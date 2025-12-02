import path from 'path'; // Built in module to manipulate file paths
import fs from 'fs'; // Built in module for working with the file system
import dotenv from 'dotenv'; // Module for loading in environment variables from an .env file
import { spawn } from 'child_process'; // Module to run the Java JAR as a child process
import { v4 as uuidv4 } from 'uuid'; // Module for generating unique job IDs
import { fileURLToPath } from 'url'; // Module for resolving ES module file paths

// Get __dirname for ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Load environment variables from the .env file in the project root
dotenv.config({ path: path.resolve(__dirname, '../../.env') });

// Access configurations from the .env file
// Default internal paths are used if there are no .env variables 
const VIDEO_DIR = process.env.VIDEO_DIR || '/videos';
const JAR_PATH = process.env.JAR_PATH || path.resolve(__dirname, '../../processor/videoprocessor.jar');
const OUTPUT_DIR = process.env.OUTPUT_DIR || '/results';
const JOBS_FILE = path.join(OUTPUT_DIR, 'jobs.json'); // Centralized jobs metadata file with jobIDs, status, and output file location

// Helper to load all job metadata from the central jobs.json file
const loadJobsData = () => {
    if (!fs.existsSync(JOBS_FILE)) return {};
    return JSON.parse(fs.readFileSync(JOBS_FILE, 'utf-8'));
};

// Helper to save all job metadata back to jobs.json
const saveJobsData = (jobsData) => {
    fs.writeFileSync(JOBS_FILE, JSON.stringify(jobsData, null, 2));
};

const startVideoProcessingJob = (req, res) => {
    // /process/:filename
    const { filename } = req.params;
    // ?targetColor=<hex>&threshold=<int>
    const { targetColor, threshold } = req.query;

    if (!targetColor || !threshold) {
        return res.status(400).json({ error: "Missing targetColor or threshold query parameter" });
    }

    // Generate a unique job ID and create a directory for the job's output
    const jobId = uuidv4();
    const jobDir = path.join(OUTPUT_DIR, jobId);
    fs.mkdirSync(jobDir, { recursive: true });

    const inputPath = path.join(VIDEO_DIR, filename);
    const outputCSV = path.join(jobDir, 'result.csv');

    // Load current jobs and add a new entry
    const jobsData = loadJobsData();
    jobsData[jobId] = {
        jobId, // Save the job ID
        status: 'processing', // Set a starting status of processing
        outputFileName: `${jobId}/result.csv`, // Save the filepath of the result.csv file
        videoFileName: filename, // Save original video file name
        targetColor: targetColor, // Save target color
        threshold: Number(threshold) // Save threshold as a number
    };
    saveJobsData(jobsData);

    // Build Java command line arguments
    const args = [
        '-jar',
        JAR_PATH,
        inputPath,
        outputCSV,
        targetColor,
        threshold
    ];

    // Try-catch block to run the JAR command
    try {
        const child = spawn('java', args);

        // Listen for successful completion
        child.on('exit', (code) => {
            const updatedJobs = loadJobsData();

            if (updatedJobs[jobId]) {
                if (code === 0) {
                    updatedJobs[jobId].status = 'done';
                } else {
                    updatedJobs[jobId].status = 'error';
                    updatedJobs[jobId].error = `Java process exited with code ${code}`;
                }

                saveJobsData(updatedJobs);
            }
        });

        return res.status(202).json({ jobId });
    } catch (err) {
        console.error("Failed to start processing job: ", err);
        return res.status(500).json({ error: "Error starting job" });
    }
};


const getProcessingJobStatus = (req, res) => {
    // Get jobId from params
    const { jobId } = req.params;

    try {
        // Get data about the jobs
        const jobsData = loadJobsData();

        // Check if the job exists in the jobs.json file
        if (!jobsData[jobId]) {
            return res.status(404).json({ error: "Job ID not found" });
        }

        // Get job information and prepare the response 
        const job = jobsData[jobId];
        const response = { status: job.status };

        // If the job has finished successfully, return the result.csv file path in the response
        if (job.status === 'done') {
            response.result = path.join('/results', job.outputFileName);
        }
        // If the job failed, return an error message in the response
        else if (job.status === 'error') {
            response.error = job.error || "Unknown error occurred";
        }

        // Return the current job status with a status code of 200
        return res.status(200).json(response);

    } catch (err) {
        console.error("Error fetching job status:", err);
        return res.status(500).json({ error: "Error fetching job status" });
    }
};

const getAllJobs = (req, res) => {
    try {
        // Load and return all jobs from the jobs.json file
        const jobsData = loadJobsData();
        const allJobs = Object.values(jobsData); // Convert from object to array
        return res.status(200).json(allJobs);
    } catch (err) {
        console.error("Error fetching all jobs:", err);
        return res.status(500).json({ error: "Error loading job list" });
    }
};


export default { startVideoProcessingJob, getProcessingJobStatus, getAllJobs };