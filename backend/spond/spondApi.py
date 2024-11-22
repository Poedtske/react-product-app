import asyncio
import datetime
import json
from time import sleep
import sys
from dotenv import load_dotenv
import os
import aiohttp  # Import aiohttp for async HTTP requests
from spond import spond

load_dotenv()

username = os.getenv('SPOND_USERNAME')
password = os.getenv('SPOND_PASSWORD')
group_id = os.getenv('SPOND_GROUP_ID')
api_base_url = os.getenv('URL')  # Fetch the base URL from the environment file
api_spond_private="/api/secure/events"
api_spond_public="/api/public/events/spond"

# Define the headers that will be used in every API call
headers = {
    "Content-Type": "application/json",
    "x-api-key": os.getenv('API_KEY')  # Replace with your actual API key
}

def add_two_hours_to_timestamp(timestamp):
    # Parse the timestamp to a datetime object, replacing 'Z' with '+00:00'
    dt = datetime.datetime.fromisoformat(timestamp.replace('Z', '+00:00'))
    
    # Add 2 hours
    dt += datetime.timedelta(hours=1)
    
    # Return the formatted timestamp in the 'YYYY-MM-DD HH:MM:SS.0' format
    return dt.strftime('%Y-%m-%d %H:%M:%S.0')

async def get_events_spond():
    s = spond.Spond(username=username, password=password)
    group = await s.get_group(group_id)
    events = await s.get_events(group_id=group_id, include_scheduled=True, min_start=datetime.datetime.now())
    eventlist = []

    for event in events:
        if 'repetitie' not in event['heading'].lower():
            event_details = {
                "spondId": event['id'],
                "title": event['heading'],
                "startTime": add_two_hours_to_timestamp(event['startTimestamp']),
                "endTime": add_two_hours_to_timestamp(event['endTimestamp']),
                "location": event['location']['feature']
            }

            # Add description if it exists
            if 'description' in event:
                event_details['description'] = event['description']

            eventlist.append(event_details)

    await s.clientsession.close()
    return eventlist

async def get_local_events():
    url = f"{api_base_url+api_spond_public}"
    async with aiohttp.ClientSession() as session:
        async with session.get(url, headers=headers) as response:
            if response.status == 200:
                await session.close()
                return await response.json()  # Assuming the response contains the events as JSON
            else:
                print(f"Failed to fetch local events. Status: {response.status}")
                await session.close()
                return []

async def delete_event_from_local(event):
    url = f"{api_base_url+api_spond_private}/spond"
    async with aiohttp.ClientSession() as session:
        async with session.delete(url, json=event, headers=headers) as response:
            if response.status == 200:
                print(f"Successfully deleted event with ID {event['id']}")
                await session.close()
                return True
            else:
                print(f"Failed to delete event with ID {event['id']}. Status: {response.status}")
                await session.close()
                return False

async def post_event_to_local(event):
    url = f"{api_base_url+api_spond_private}"
    async with aiohttp.ClientSession() as session:
        async with session.post(url, json=event, headers=headers) as response:
            if response.status == 200:
                print(f"Successfully added event with ID {event['spondId']}")
                await session.close()
                return True
            else:
                print(f"Failed to add event with ID {event['spondId']}. Status: {response.status}")
                await session.close()
                return False

async def update_event_in_local(event):
    url = f"{api_base_url+api_spond_private}/spond"
    async with aiohttp.ClientSession() as session:
        async with session.put(url, json=event, headers=headers) as response:
            if response.status == 200:
                print(f"Successfully updated event with ID {event['spondId']}") 
                await session.close()   
                return True           
            else:
                print(f"Failed to update event with ID {event['spondId']}. Status: {response.status}")
                await session.close()
                return False

async def compare_and_update_events(spond_events, local_events):
    local_events_map = {event['spondId']: event for event in local_events if 'spondId' in event}
    
    # Handle deletions: Events that are in local but not in spond
    for local_event in local_events:
        if 'spondId' in local_event:
            if local_event['spondId'] not in {event['spondId'] for event in spond_events}:
                print(f"Event {local_event['spondId']} is no longer in Spond, deleting locally.")                
                boolean = await delete_event_from_local(local_event)
                if boolean:
                    local_events.remove(local_event)

    # Handle additions and updates: Events in Spond but not local, or different
    for spond_event in spond_events:
        local_event = local_events_map.get(spond_event['spondId'])

        if not local_event:
            print(f"Event {spond_event['spondId']} does not exist locally, adding it.")
            boolean = await post_event_to_local(spond_event)
            if boolean:
                local_events.append(spond_event)
        elif local_event != spond_event:
            print(f"Event {spond_event['spondId']} differs locally, updating it.")
            print(local_event)
            print("----------------------------------------------")
            print(spond_event)
            boolean = await update_event_in_local(spond_event)
            if boolean:
                local_event = spond_event

async def main():
    sleep(60)
    print("Fetching Spond events and comparing with local events...")
    local_events = []
    firsttime = True
    while True:
        try:
            if firsttime:
                print('fetch local events')
                local_events = await get_local_events()
                print('fetch spond events')
                spond_events = await get_events_spond()
                print('compare')
                await compare_and_update_events(spond_events, local_events)
                firsttime = False
            else:
                print('fetch spond events first time')
                spond_events = await get_events_spond()
                print('compare first time')
                await compare_and_update_events(spond_events, local_events)

        except Exception as e:
            print(f"Error occurred: {e}")
            sys.exit(1)  # Exit the script and stop the container

        await asyncio.sleep(5)  # Add a small sleep to prevent tight loops
        print("Done. Sleeping for 24 hours.")
        sleep(24 * 60 * 60)  # Sleep for 24 hours

if __name__ == "__main__":
    asyncio.run(main())
