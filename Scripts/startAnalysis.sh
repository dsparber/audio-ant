#!/bin/bash

R CMD Rserve --save
cd /home/pi/HardwareController/
sudo -b python connection.py
cd /home/pi/
java -jar /home/pi/AudioRecognition.jar &
