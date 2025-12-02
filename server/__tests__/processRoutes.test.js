import request from 'supertest'; // Import for using request from supertest
import app from '../index.js'; // Import app from index.js

// Test suite for processing routes
describe('Process Controller Routes', () => {
    // Test inputs
    const testVideo = 'Salamander5Seconds.mp4';
    const targetColor = '2D0508';
    const threshold = 180;

    let jobId = null; // jobId that will be returned from the server

    // Test suite for POST /process/:filename route
    describe('POST /process/:filename', () => {
        // Test Case 1: Missing query parameters
        it('should return 400 if query params are missing', async () => {
            const response = await request(app).post(`/process/${testVideo}`);

            // Expecting a status code of 400 Bad Request and in error property in the response
            expect(response.status).toBe(400);
            expect(response.body).toHaveProperty('error');
        });

        // Test Case 2: Proper request to start a job
        it('should start a processing job and return a jobId', async () => {
            const response = await request(app)
                .post(`/process/${testVideo}?targetColor=${targetColor}&threshold=${threshold}`);

            // Expecting a status code of 202 Accepted and a jobId
            expect(response.status).toBe(202);
            expect(response.body).toHaveProperty('jobId');

            // Storing the jobId for the getJobStatus test case
            jobId = response.body.jobId;
        });
    });

    // Test suite for GET /process/:jobId/status route
    describe('GET /process/:jobId/status', () => {
        // Test Case 1: Should return 404 if jobId does not exist
        it('should return 404 for a non-existent jobId', async () => {
            const response = await request(app).get('/process/nonexistentjobid/status');

            // Expecting a 404 Not Found status and an error property in the response
            expect(response.status).toBe(404);
            expect(response.body).toHaveProperty('error');
        });

        // Test Case 2: Should return 200 with status: processing, done, or error
        it('should return job status as processing, done, or error', async () => {
            // Skip this test if jobId was not set from the POST test
            if (!jobId) return;

            const response = await request(app).get(`/process/${jobId}/status`);

            // Expecting a status code of 200 OK and a status property with one of the valid job states
            expect(response.status).toBe(200);
            expect(response.body).toHaveProperty('status');
            expect(['processing', 'done', 'error']).toContain(response.body.status);
        });

        // Test Case 3: Should return result path if job is done OR error message if job failed
        it('should return result if done, or error message if job failed', async () => {
            // Skip this test if jobId was not set from the POST test
            if (!jobId) return;

            let response;

            // Keep polling until the job completes or errors out, up to a max number of tries
            for (let i = 0; i < 10; i++) {
                response = await request(app).get(`/process/${jobId}/status`);
                if (response.body.status === 'done' || response.body.status === 'error') break;
                await new Promise(res => setTimeout(res, 500)); // Wait 0.5 seconds between polls
            }

            // Expecting a final status of either 'done' or 'error'
            expect(response.status).toBe(200);
            expect(['done', 'error']).toContain(response.body.status);

            // If job is done, expect a result property with a string ending in .csv
            if (response.body.status === 'done') {
                expect(response.body).toHaveProperty('result');
                expect(typeof response.body.result).toBe('string');
                expect(response.body.result.endsWith('.csv')).toBe(true);
            }
            // If job errored out, expect an error message string
            else if (response.body.status === 'error') {
                expect(response.body).toHaveProperty('error');
                expect(typeof response.body.error).toBe('string');
            }
        });
    });

    // Test suite for the GET /jobs route
    describe('GET /jobs', () => {
        // Test Case 1: Should return 200 and an array of job entries
        it('should return a list of all jobs in jobs.json', async () => {
            const response = await request(app).get('/jobs');

            // Expecting a 200 OK status
            expect(response.status).toBe(200);

            // Response body should be an array
            expect(Array.isArray(response.body)).toBe(true);

            // If jobs exist, validate structure of first job object
            if (response.body.length > 0) {
                const job = response.body[0];

                // Always-required fields
                expect(job).toHaveProperty('jobId');
                expect(job).toHaveProperty('status');
                expect(job).toHaveProperty('outputFileName');

                // New fields — only test if they exist
                if ('videoFileName' in job) {
                    expect(typeof job.videoFileName).toBe('string');
                    expect(typeof job.targetColor).toBe('string');
                    expect(typeof job.threshold).toBe('number');
                }
            }
        });
    });

});