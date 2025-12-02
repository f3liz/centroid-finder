import express from 'express';
import videoController from '../controllers/videoController.js';
import processController from '../controllers/processController.js';

const router = express.Router();

router.get("/api/videos", videoController.getAllVideos);
router.get("/thumbnail/:filename", videoController.getVideoThumbnail);
router.post("/process/:filename", processController.startVideoProcessingJob);
router.get("/process/:jobId/status", processController.getProcessingJobStatus);
router.get("/jobs", processController.getAllJobs);

export default router;