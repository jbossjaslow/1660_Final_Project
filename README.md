# 1660_Final_Project

## This is the Inverted Indices option of the final project for 1660

#### In this project, I had to develop several programs that ran MapReduce on an HDFS cluster on GCP. The programs were creating an inverted indices data structure, searching for a particular word, and finding the top n words in the list of files given.
#### I also had to write a local GUI program to allow the user to interact and run these cloud programs, and then aggregate the data into an easily viewable form.

## Steps to Reproduce (on macOS)
* Download and install Docker Desktop, and then ensure it is running
    * Then, run the following command to pull the image from Docker Hub
    * `docker pull superjew102/1660_final_project`
* Download, install, and run Xquartz
* On the Xquartz terminal, navigate to the folder where you downloaded the image from Docker and run the following commands:
    * `IP=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')`
    * `xhost + $IP`
    * `docker run --rm -e DISPLAY=$IP:0 -v /tmp/.X11-unix:/tmp/.X11-unix --name ny-name invertedindices`
* The program should open in a new Xquartz window and you should be able to interact with it
* While running the program, it can take a minute or two to run the cloud programs and return back with the data. Therefore, keep an eye on the Xquartz terminal, as it will continuously report the status of the job while it is running

## Click <https://pitt-my.sharepoint.com/:v:/g/personal/joj43_pitt_edu/EW6SX_KydQJHheR2D7lLx9QBW1A9qQ8rpI3moJ6wRSOIhQ?e=hk7QmV> to view the video of the project walkthrough on OneDrive
## If OneDrive is making the video too blurry or pixelated, here's a [YouTube link](https://youtu.be/xt3q6LfdEwc) with my video which may be easier to watch (I recommend viewing in 1080p on full screen to properly see all of the text)
