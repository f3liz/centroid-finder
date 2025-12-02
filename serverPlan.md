# Server Application Plan

## Project Overview

The goal is to build an application that communicates with the back-end server to handles API requests for the Salamander Tracker system. The application should support:

- **GET requests**: Return data such as a list of available videos for processing.
- **POST requests**: Accept video processing requests and run our previously developed JAR file.
- **Job tracking**: Monitor the status of each processing job (we're still deciding whether to use a database or the filesystem for this).
- **Result delivery**: Return a downloadable CSV file containing the processed data and possibly an image of the first frame of the video for user preview.

## Implementation Strategy

We'll implement the separate routes outlined in the salamander-api for each required functionality:

- Handling GET and POST requests.
- Triggering the video processing JAR.
- Tracking job progress and status.
- Serving output files and other relevant responses.

## Testing and Validation

Once the routes are implemented, we will conduct comprehensive testing to ensure all endpoints and application logic work as we intended.
Use Jest to test JavaScript backend. Test for status codes and other stuff.