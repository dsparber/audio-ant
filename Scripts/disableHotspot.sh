#!/bin/bash

sudo cp /etc/dhcp/dhcpd.conf.bak /etc/dhcp/dhcpd.conf
sudo cp /etc/default/isc-dhcp-server.bak /etc/default/isc-dhcp-server
sudo cp /etc/network/interfaces.bak /etc/network/interfaces
sudo service isc-dhcp-server stop
sudo ifdown wlan0
sudo ifup wlan0
