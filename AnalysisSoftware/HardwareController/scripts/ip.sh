#!/bin/bash

ssid=`ifconfig wlan0 | grep -o "inet addr:[0-9.]*"`
ssid=${ssid:10}
echo $ssid
