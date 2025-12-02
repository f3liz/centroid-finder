import express from 'express';
import router from "./routes/routers.js"
import cors from 'cors';
import path from 'path';
import dotenv from 'dotenv';
import { fileURLToPath } from 'url'; // for resolving ES module paths

// Get __dirname from ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Load environment variables from the .env file in the project root
dotenv.config({ path: path.resolve(__dirname, '../.env') });

// Access configuration from the .env file
const outputDir = path.resolve(__dirname, process.env.OUTPUT_DIR || '/results');

const app = express();

// Default port is used if there is no .env variable
const PORT = process.env.PORT || 3000;

app.use(cors());

// Read in JSON payloads in request body
app.use(express.json());

// No longer needed: Serve frontend static files (built React app)
// app.use(express.static(path.join(__dirname, 'public')));

// Serve the output directory statically
app.use('/results', express.static(outputDir));

// Serve routes through the router
app.use("/", router);

// Only listen if not in test mode
if (process.env.NODE_ENV !== 'test') {
    app.listen(PORT, () => {
        console.log(`Listening on http://localhost:${PORT}`);
    });
}

export default app; // Export the app for testing