import fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url'; // for resolving ES module paths
import ffmpeg from 'fluent-ffmpeg';
import ffmpegInstaller from '@ffmpeg-installer/ffmpeg'; // need to run through npm and not install locally
import dotenv from 'dotenv'; // Module for loading in environment variables from an .env file

// for ffmpegInstaller
ffmpeg.setFfmpegPath(ffmpegInstaller.path);

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Load environment variables from the .env file in the project root
dotenv.config({ path: path.resolve(__dirname, '../../.env') });

// Access video directory from the .env file
// Default internal path is used if there is no .env variable
const VIDEO_DIR = process.env.VIDEO_DIR || '/videos';

const getAllVideos = async (req, res) => {
  try {
    const files = await fs.readdir(VIDEO_DIR);

    // get all the .mp4 videos 
    const videos = files.filter(file => file.toLowerCase().endsWith('.mp4'));

    res.status(200).json(videos);
  } catch (err) {
    console.error('Error reading video directory:', err);
    res.status(500).json({ error: 'Error reading video directory' });
  }
};

const getVideoThumbnail = async (req, res) => {
  try {
    // get filename from the params in the url
    const { filename } = req.params;

    const videoPath = path.join(VIDEO_DIR, filename);

    // access the file
    await fs.access(videoPath);

    // set content-type
    res.setHeader('Content-Type', 'image/jpeg');

    // use ffmpeg to get first frame and set back as .jpg
    ffmpeg(videoPath)
      .on('error', (err) => {
        console.error('FFmpeg error:', err);
        // send 500 if it fails
        res.status(500).json({ error: 'Error generating thumbnail' });
      })
      .frames(1) // first frame
      .format('image2') // needed to output a single image
      .output(res) // send to response
      .run();

  } catch (err) {
    // for other errors such as file not found
    res.status(500).json({ error: "Error generating thumbnail" });
  }
};

export default { getAllVideos, getVideoThumbnail };