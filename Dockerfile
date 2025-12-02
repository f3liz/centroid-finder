# Create the base image for Java with OpenJDK 25
FROM openjdk:25-slim

# Install Curl to fetch Node.js, Git, and other necessary tools
RUN apt-get update && \
    apt-get install -y curl gnupg git && \
    curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Set working directory in the container to a new directory called app
WORKDIR /app

# Copy the .env file into the container
COPY .env .env

# Remove a previously cloned frontend if it was already cloned
RUN rm -rf frontend

# Clone the React frontend repo (built with Next.js)
RUN git clone https://github.com/f3liz/centroid-finder-frontend.git frontend

# Set the working directory to frontend and install and build the Next.js app
WORKDIR /app/frontend
RUN npm install && npm run build

# Go back to the working directory of the app
WORKDIR /app

# Copy backend's package.json and package-lock.json
COPY server/package*.json ./server/

# Omit the dev dependencies with npm clean install
RUN cd server && npm ci --omit=dev

# Copy the backend source code
COPY server ./server

# Copy the Java processor JAR file
COPY processor/videoprocessor.jar ./processor/videoprocessor.jar

# Expose port 3000 for the Express backend server and 3001 for the frontend
EXPOSE 3000 3001

# Install concurrently to run both the frontend and backend concurrently
RUN npm install -g concurrently

# Switch to the final working directory
WORKDIR /app

# Start both the backend and frontend server together
CMD ["concurrently", "node server/index.js", "npm --prefix frontend start"]