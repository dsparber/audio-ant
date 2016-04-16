#!/bin/bash

R CMD Rserve --save
cd /home/pi/HardwareController/
sudo -b python connection.py
cd /home/pi/
sudo -b java -jar /home/pi/AudioAnalysis.jar
