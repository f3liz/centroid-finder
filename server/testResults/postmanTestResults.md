# Video Controller

## getAllVideos
- Request = GET http://localhost:3000/api/videos
- Result = [
    "Salamander5Seconds.mp4"
    ]

## getVideoThumbnail
- Request = GET http://localhost:3000/thumbnail/Salamander5Seconds.mp4
- Result = 

![Thumbnail Screenshot](./screenshots/thumbnail-response.jpg)

# Process Controller

## startVideoProcessingJob
- Request = POST http://localhost:3000/process/Salamander5Seconds.mp4?targetColor=2D0508&threshold=180
- Result = {
    "jobId": "1661e2b8-2f0b-47be-8317-bc17c1e9a270"
    }

## getProcessingJobStatus
- Request = GET http://localhost:3000/process/1661e2b8-2f0b-47be-8317-bc17c1e9a270/status
- Result 1 = {
    "status": "processing"
    }   
- Result 2 = {
    "status": "done"
    } 