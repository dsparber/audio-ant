#!/bin/bash

ssid=`iwconfig wlan0 | grep -o "ESSID:\"[a-zA-Z\ ]*\""`
ssid=${ssid:7:-1}
echo $ssid
