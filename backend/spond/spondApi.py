import asyncio
import datetime
from spond import spond  # Assuming spond is a package you have installed
import json
import sys
from dotenv import load_dotenv
import os

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
    events = await s.get_events(group_id=group_id,include_scheduled=True, min_start=datetime.datetime.now())
    eventlist = []

    for event in events:
        if 'repetitie' not in event['heading'].lower():
            eventlist.append(event)

    event_dict = {}
    for index, event in enumerate(eventlist):
        event_details = {
            "ID":event['id'],
            "title": event['heading'],
            "start": add_two_hours_to_timestamp(event['startTimestamp']),
            "end": add_two_hours_to_timestamp(event['endTimestamp']),
            "location": event['location']['feature']
        }

        # Add description if it exists
        if 'description' in event:
            event_details['description'] = event['description']

        event_dict[index] = event_details

    # print(group['name'])

    await s.clientsession.close()
    return event_dict

if __name__ == "__main__":
    events = asyncio.run(get_events_spond())
    json_output = json.dumps(events, ensure_ascii=False, indent=4)
    sys.stdout.buffer.write(json_output.encode('utf-8'))
