#!/bin/bash

sudo cp /etc/dhcp/dhcpd.conf.ap /etc/dhcp/dhcpd.conf
sudo cp /etc/default/isc-dhcp-server.ap /etc/default/isc-dhcp-server
sudo cp /etc/network/interfaces.ap /etc/network/interfaces
sudo ifdown wlan0
sudo ifup wlan0
sudo service hostapd restart
sudo service isc-dhcp-server restart
sudo update-rc.d hostapd enable
sudo update-rc.d isc-dhcp-server enable
