#!/bin/bash

ap=`service hostapd status | grep -o "Active:\ [a-zA-Z]*"`
ap=${ap:8}
echo $ap
