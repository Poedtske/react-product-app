import asyncio
import datetime
from spond import spond  # Assuming spond is a package you have installed
import json
import sys
from dotenv import load_dotenv
import os
import aiohttp  # Import aiohttp for async HTTP requests

load_dotenv()

username = os.getenv('SPOND_USERNAME')
password = os.getenv('SPOND_PASSWORD')
group_id = os.getenv('SPOND_GROUP_ID')

def add_two_hours_to_timestamp(timestamp):
    # Parse the timestamp
    dt = datetime.datetime.fromisoformat(timestamp.replace('Z', '+00:00'))
    # Add 2 hours
    dt += datetime.timedelta(hours=2)
    # Convert back to ISO format with 'Z'
    return dt.isoformat().replace('+00:00', 'Z')

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

async def post_events_to_api(events):
    url = "http://localhost:8080/api/events/batch"
    headers = {
        "Content-Type": "application/json"
    }
    async with aiohttp.ClientSession() as session:
        async with session.post(url, data=json.dumps(events), headers=headers) as response:
            if response.status == 200:
                print("Events posted successfully!")
                response_text = await response.text()
                print("Response:", response_text)
            else:
                print(f"Failed to post events. Status: {response.status}")
                response_text = await response.text()
                print("Error Response:", response_text)

if __name__ == "__main__":
    events = asyncio.run(get_events_spond())
    asyncio.run(post_events_to_api(events))
