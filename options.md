1. OpenCV
2. VLCJ
3. JCodec

Pros and Cons of OpenCV
Pros:
1. Built in powerful tracking algorithms
2. Has frame by frame access for video processing
3. Optimized for accuracy and speed in vision tasks

Cons:
1. Requires native binaries (needs setup via JavaCV or OpenCV)
2. Complex for new users, hard to learn
3. Not good for viewing just good at analysis

Pros and Cons of VLCJ
Pros:
1. Good MP4 support (which is our video format)
2. Able to access frames in real time
3. Easy GUI integration

Cons:
1. No tracking (needs external libraries for this)
2. Needs VLC installed and configured on device
3. Not really intended for analysis


Pros and Cons of JCodec
Pros:
1. Strictly Java
2. Simple in extracting frames from MP4
3. Easy set up

Cons:
1. No built in tracking algorithm with JCodec
2. Limited Codec support
3. Slow performance for long MP4 videos or high res videos