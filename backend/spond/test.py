import requests

# Define the URL for the GET request
url = "http://localhost:8080/drinks"  # Replace with the actual URL of the API

# Make the GET request
response = requests.get(url)

# Check if the request was successful
if response.status_code == 200:
    # Print the JSON response (if the API returns JSON data)
    print(response.json())
else:
    # Print the error message if the request failed
    print(f"Error: {response.status_code}, {response.text}")
