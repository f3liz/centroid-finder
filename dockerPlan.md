# Docker Plan for Salamander Full-Stack Application

## Overall Goal:

The goal is to package the entire **Salamander Full-Stack Application** into a single Docker image that anyone can run with zero setup. This one image will include the frontend, backend, and the Java processor all working together. Once published to GHCR (GitHub Container Registry), anyone will be able to pull it and run it with a single `docker run` command.

## Implementation Strategy:

- **Structure**: Instead of splitting the app into multiple containers, we’ll use a **single container** approach. Everything will live in one image:
  1. **Frontend** – Cloned directly from GitHub and built inside the image
  2. **Backend** – Express API that serves both the frontend and endpoints
  3. **Java Processor** – Included as a `.jar` file and called from the backend when needed

- The Docker image will expose one main port (`3000`) for users to access the app.
- The video and results folders will be **mounted as volumes** at runtime to allow file input/output without changing anything inside of the container.

### What base Docker image will you use?

We'll start with `openjdk:25-slim` and then install **Node.js manually** inside the same image. This gives us Java support for the processor and Node.js support for the backend and frontend build.

### How will you make sure that both Node.js and Java can run?

We’ll install Node using the official setup script from NodeSource during the Docker build. Since the container already includes Java from the base image, this gives us both environments in one container with no extra layers.

### How will you test your Dockerfile and image?

We’ll:
- Build and run the image locally using `docker build` and `docker run`
- Use **Postman** to test backend endpoints
- Test end-to-end flows like uploading a video and receiving a processed CSV
- Mount test folders to `/videos` and `/results` to confirm correct input/output

### How will you make sure the endpoints are available outside the image?

We’ll expose port `3000` in the Dockerfile and use `-p 3000:3000` in the `docker run` command. This way, the full app is available at `http://localhost:3000` from the host machine.

### How will your code know where to access the video/results directory?

We’ll mount folders into the container using:
-v "$VIDEO_DIRECTORY:/videos" -v "$RESULTS_DIRECTORY:/results"

Inside the backend code, we’ll reference `/videos` and `/results` directly or with environment variables so they can be swapped out easily.

### How can you make your Docker image small, cacheable, and quick to update?

We’re planning to:
- Use `slim` base images
- Use `.dockerignore` to leave out unnecessary stuff (like `.git`, `node_modules`, test files, etc.)
- Install only production dependencies in the backend using `npm ci --omit=dev`

## Testing and Validation:

We'll manually test the full application by:
- Accessing `localhost:3000` to view the frontend
- Using Postman to reach `/api/videos` and other routes
- Making sure a video gets processed correctly and results show up in `/results`
- Checking that everything runs correctly without needing to install anything locally