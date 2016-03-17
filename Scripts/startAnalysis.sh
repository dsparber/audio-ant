#!/bin/bash
cd /home/pi

R CMD Rserve --save
sudo -b python /home/pi/HardwareController/connection.py
java -jar /home/pi/AudioRecognition.jar &
