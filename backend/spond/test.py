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
api_spond_private = "/api/secure/events"
api_spond_public = "/api/public/events/spond"

# Define the headers that will be used in every API call
headers = {
    "Content-Type": "application/json",
    "x-api-key": os.getenv('API_KEY')  # Replace with your actual API key
}

def add_two_hours_to_timestamp(timestamp):
    dt = datetime.datetime.fromisoformat(timestamp.replace('Z', '+00:00'))
    dt += datetime.timedelta(hours=2)
    return dt.strftime('%Y-%m-%d %H:%M:%S.0')

async def get_events_spond():
    s = spond.Spond(username=username, password=password)
    group = await s.get_group(group_id)
    events = await s.get_events(group_id=group_id, include_scheduled=True, min_start=datetime.datetime.now())
    await s.clientsession.close()  # Close the session after use
    eventlist = []

    for event in events:
        try:
            if 'repetitie' not in event['heading'].lower() or ('repetitie' in event['heading'].lower() and 'open' in event['heading'].lower()):
                event_details = {
                    "spondId": event['id'],
                    "title": event['heading'],
                    "startTime": add_two_hours_to_timestamp(event['startTimestamp']),
                    "endTime": add_two_hours_to_timestamp(event['endTimestamp']),
                    # Safely access location feature (check if 'location' and 'feature' exist)
                    "location": event.get('location', {}).get('feature', 'No Location Available')
                }
                if 'description' in event:
                    event_details['description'] = event['description']
                eventlist.append(event_details)
        except KeyError as e:
            # Print the event ID and the specific error
            print(f"Error processing event ID {event['id']}: Missing key {e}")
        except Exception as e:
            # Catch other unexpected errors and print the event ID
            print(f"Error processing event ID {event['id']}: {e}")

    return eventlist

async def get_local_events():
    url = f"{api_base_url+api_spond_public}"
    print(f"GET {url}")
    print(f"Headers: {headers}")
    return []  # Simulating response without sending request

async def delete_event_from_local(event):
    url = f"{api_base_url+api_spond_private}/spond"
    print(f"DELETE {url}")
    print(f"Data: {json.dumps(event, indent=2)}")
    return True  # Simulating success

async def post_event_to_local(event):
    url = f"{api_base_url+api_spond_private}"
    print(f"POST {url}")
    print(f"Data: {json.dumps(event, indent=2)}")
    return True  # Simulating success

async def update_event_in_local(event):
    url = f"{api_base_url+api_spond_private}/spond"
    print(f"PUT {url}")
    print(f"Data: {json.dumps(event, indent=2)}")
    return True  # Simulating success

async def compare_and_update_events(spond_events, local_events):
    local_events_map = {event['spondId']: event for event in local_events if 'spondId' in event}

    for local_event in local_events:
        if 'spondId' in local_event and local_event['spondId'] not in {event['spondId'] for event in spond_events}:
            print(f"Would delete event {local_event['spondId']}")
            await delete_event_from_local(local_event)

    for spond_event in spond_events:
        local_event = local_events_map.get(spond_event['spondId'])
        if not local_event:
            print(f"Would add event {spond_event['spondId']}")
            await post_event_to_local(spond_event)
        elif local_event != spond_event:
            print(f"Would update event {spond_event['spondId']}")
            await update_event_in_local(spond_event)

async def main():
    print("Fetching Spond events and comparing with local events...")
    local_events = []
    firsttime = True
    while True:
        try:
            if firsttime:
                local_events = await get_local_events()
                spond_events = await get_events_spond()
                await compare_and_update_events(spond_events, local_events)
                firsttime = False
            else:
                spond_events = await get_events_spond()
                await compare_and_update_events(spond_events, local_events)
        except Exception as e:
            print(f"Error occurred: {e}")
            sys.exit(1)
        await asyncio.sleep(5)
        print("Done. Sleeping for 24 hours.")
        sleep(24 * 60 * 60)

if __name__ == "__main__":
    asyncio.run(main())