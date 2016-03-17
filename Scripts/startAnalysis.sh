#!/bin/bash
cd /home/pi/HardwareController/

R CMD Rserve --save
sudo -b connection.py
java -jar /home/pi/AudioRecognition.jar &
