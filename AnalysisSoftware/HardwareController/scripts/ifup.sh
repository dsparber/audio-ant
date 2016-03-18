#!/bin/bash

state=`ip link show wlan0 | grep -o "UP"`
if [[ $state == UP* ]]
then
	echo "True"
else
	echo "False"
fi
