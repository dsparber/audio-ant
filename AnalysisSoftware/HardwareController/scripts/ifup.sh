#!/bin/bash

down=`ip link show wlan0 | grep -o "state UP"`
if [ "$down" == "state UP" ]
then
	echo "True"
else
	echo "False"
fi
