import request from 'supertest'; // Import for using request from supertest
import app from '../index.js'; // Import app from index.js

// Test suite for video routes
describe('Video Controller Routes', () => {

    // Tests for the GET /api/videos route
    describe('GET /api/videos', () => {
        it('should return an array of video filenames', async () => {
            const response = await request(app).get('/api/videos');

            // Expecting a status code of 200 OK
            expect(response.status).toBe(200);

            // Expecting that the response body is an array
            expect(Array.isArray(response.body)).toBe(true);
        });
    });

    // Tests for the GET /thumbnail/:filename route
    describe('GET /thumbnail/:filename', () => {
        const existingFile = 'Salamander5Seconds.mp4'; // A real video in public/videos
        const nonExistentFile = 'notfound.mp4'; // A fake video for testing

        it('should return 500 if video file does not exist', async () => {
            // Requesting a thumbnail for a non-existent video
            const response = await request(app).get(`/thumbnail/${nonExistentFile}`);

            // Expecting that a status code of 500 with a proper error message is returned
            expect(response.status).toBe(500);
            expect(response.body).toHaveProperty('error', 'Error generating thumbnail');
        });

        it('should return 200 and a JPEG image if the file exists', async () => {
            // Requesting a valid video thumbnail
            const response = await request(app).get(`/thumbnail/${existingFile}`);

            // Expecting a status code of 200 Ok and a JPEG is returned
            expect(response.status).toBe(200);
            expect(response.headers['content-type']).toMatch(/jpeg/);
        });
    });
});