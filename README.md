# Salamander Tracker – Backend + Java Batch Processor

This is the Express.js backend and Java batch processor for the Salamander Tracker app. It provides API endpoints to manage video processing jobs, communicate with the frontend, and generate CSV files based on centroid data extracted from video frames.


## Setup Instructions (with Docker)

### Option 1: Run Prebuilt Image from GHCR (Recommended)

```bash
docker run \
  -p 3000:3000 \
  -p 3001:3001 \
  -v "$VIDEO_DIRECTORY:/videos" \   # Path to local video files
  -v "$RESULTS_DIRECTORY:/results" \ # Path to save CSV output
  ghcr.io/alexanderoruban/salamander:latest
```

### Option 2: Build Image Locally (For Developers)

```bash
docker build --no-cache -t ghcr.io/alexanderoruban/salamander .
docker run \
  -p 3000:3000 \
  -p 3001:3001 \
  -v "$VIDEO_DIRECTORY:/videos" \
  -v "$RESULTS_DIRECTORY:/results" \
  ghcr.io/alexanderoruban/salamander:latest
```

The backend will be available at http://localhost:3000, and the frontend (cloned and built during the Docker process) will be available at http://localhost:3001.


## Directory Structure

- server/ – Express.js API server

- processor/ – Java batch processor (video-processing logic)

- frontend/ – Cloned and built automatically during Docker image creation

- jobs.json – Tracks the status of processing jobs

- videos/ – Mounted volume containing input videos

- results/ – Mounted volume containing output CSV files


## API Endpoints

- GET /thumbnail/:filename – Returns a video frame as thumbnail

- POST /process – Starts processing a given video file

- GET /status/:jobId – Returns the processing status

- GET /results/:filename – Returns a downloadable CSV file

- GET /jobs – Lists all submitted processing jobs


## Technologies Used

- [Node.js](https://nodejs.org/en) – JavaScript runtime

- [Express.js](https://expressjs.com/) – Web framework for API routing

- [Java](https://www.java.com/en/) – Batch processing engine

- [Docker](https://www.docker.com/) – Containerization for backend/frontend/integration

- [Jest](https://jestjs.io/) - JavaScript testing framework for backend unit tests


## Special Notes

- The backend automatically clones and builds the frontend repo during the Docker build.

- The Java processor is run as a subprocess when a job is submitted.

- Make sure the videos/ and results/ directories are mounted when running the container to persist inputs and outputs.


## Related Repositories
- [Salamander Tracker - Frontend (Next.js)](https://github.com/f3liz/centroid-finder-frontend)


## License
MIT License - see [LICENSE](./LICENSE) for details.