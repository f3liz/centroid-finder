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
  ghcr.io/f3liz/salamander:latest
```

### Option 2: Build Image Locally (For Developers)

```bash
docker build --no-cache -t ghcr.io/f3liz/salamander .
docker run \
  -p 3000:3000 \
  -p 3001:3001 \
  -v "$VIDEO_DIRECTORY:/videos" \
  -v "$RESULTS_DIRECTORY:/results" \
  ghcr.io/f3liz/salamander:latest
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

## Automated Workflow Pipelines

This repository uses GitHub Actions to automate testing, building, and deployment.

The pipelines are structured to validate the backend, Java batch processor, and frontend together as a full-stack application before anything is built and deployed.

### Automated Testing Pipeline

**Triggered on:**
- Pushes to `main` and `development`
- Pull requests on `main` or `development`

Runs as a multi-stage workflow covering all major components:

#### Backend Testing
- Installs backend dependencies.
- Creates required runtime directories for videos and job output.
- Runs Jest unit tests against Express.js routes, controllers, and job logic.

#### Java Processor Testing
- Uses Java 21 (Temurin).
- Caches Maven dependencies for faster builds.
- Runs Maven tests for the centroid finder / video processor logic.

#### Frontend End-to-End Testing
- Clones the frontend repository.
- Builds a temporary full-stack Docker image using `Dockerfile.prod`.
- Runs backend and frontend together in a container.
- Uses Cypress to perform headless E2E tests against the running app.
- Ensures frontend, backend, and Java processor integrate correctly.

Only if **all test stages pass** does the workflow allow the downstream build and deployment pipelines to continue.

### Automated Build Pipeline

- Runs automatically after the Automated Testing Pipeline completes successfully.
- Only triggers when changes are applied to `main` (direct push or merged pull request).
- Uses a multi-stage production Docker build:
  - Builds the Java processor JAR using Maven.
  - Installs and prepares the Express backend.
  - Clones, configures, and builds the frontend with the correct API URL.
- Produces a single production-ready Docker image containing:
  - Express backend
  - Java batch processor JAR
  - Built frontend bundle
- Pushes the final image to GitHub Container Registry (GHCR).

### Automated Deployment Pipeline

- Triggers after a successful production build.
- Only deploys when the build was generated from `main`.
- Connects to the deployment VM via SSH.
- Pulls the latest Docker image from GHCR.
- Removes any cached or running containers.
- Starts a fresh container with mounted volumes to persist:
  - Uploaded videos
  - Generated CSV job results
- Exposes backend on port `3000` and frontend on port `3001`.

Deployment and build tests are run after frontend changes to ensure backend and Java processor integration remains intact.


## Related Repositories
- [Salamander Tracker - Frontend (Next.js)](https://github.com/f3liz/centroid-finder-frontend)


## License
MIT License - see [LICENSE](./LICENSE) for details.