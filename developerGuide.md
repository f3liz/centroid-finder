  # Salamander Tracker – Developer Guide  
  
  This guide contains notes, commands, and instructions for local development, testing, and managing Docker builds for the Salamander Tracker backend and Java processor.

  
  ## Environment Setup
  
  ### Using ENV Variables for Local Development
  
  - Create a `.env` file in the root directory based on the provided `.env.example`.
  
  
  ## Running Tests
  
  ### Jest Tests (Node.js)
  
  ```bash
  cd server
  npm run test:env
  ```
  
  ### Java JAR Test Command
  
  To run the processor JAR locally for a test video:
  
  ```bash
  cd processor
  java -jar videoprocessor.jar ensantina.mp4 output.csv 2D0508 180
  ```
  
  
  ## Docker Build & Push
  
  ### Build with No Cache (Development)
  
  ```bash
  docker build --no-cache -t ghcr.io/alexanderoruban/salamander .
  ```
  
  ### Push to GHCR (Intel-only)
  
  ```bash
  docker push ghcr.io/alexanderoruban/salamander:latest
  ```
  
  ### Cross-platform Build & Push (Intel + Apple M1/M2/M3/M4)
  
  ```bash
  docker buildx build \
    --no-cache \
    --platform linux/amd64,linux/arm64 \
    -t ghcr.io/alexanderoruban/salamander:latest \
    --push \
    .
  ```
  
  
  ## Docker Run Commands
  
  ### Run Backend Only
  
  1. Create local directories:
     - `test_videos/` (input videos)
     - `test_results/` (output CSV files)
  
  2. Place videos in `test_videos/`.
  
  3. Run:
  
  ```bash
  docker run \
    -p 3000:3000 \
    -v "$PWD/test_videos:/videos" \
    -v "$PWD/test_results:/results" \
    ghcr.io/alexanderoruban/salamander:latest
  ```
  
  
  ### Run Backend + Frontend
  
  Same setup as above, but with frontend exposed:
  
  ```bash
  docker run \
    -p 3000:3000 \
    -p 3001:3001 \
    -v "$PWD/test_videos:/videos" \
    -v "$PWD/test_results:/results" \
    ghcr.io/alexanderoruban/salamander:latest
  ```
  
  
  ## Tips
  
  - Use `--no-cache` to force fresh frontend clone/build during image creation.
  - Remember to mount correct volume paths to persist job data and results.
  
  
  ## Related Docs
  
  - [Main README – Build & Run Overview](./README.md)
  - [Frontend Repository](https://github.com/f3liz/centroid-finder-frontend)