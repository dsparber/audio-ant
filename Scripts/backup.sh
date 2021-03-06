#!/bin/bash
cd /tmp/

# variables
backupName="backup"
packageLists="packageLists"

# current time
now="$(date +'%Y-%m-%d_%H-%M-%S')"
echo "Backup process stared: (timestamp: $now):"

# create backup folder
echo "Copying contents..."
mkdir $backupName

# copying files
sudo cp -r /etc/ $backupName
sudo cp -r /home/ $backupName

# creating and copying package list
mkdir $backupName/$packageLists
dpkg --get-selections > $backupName/$packageLists/package.list
sudo cp /etc/apt/sources.list $backupName/$packageLists/sources.list
apt-key exportall > $backupName/$packageLists/repo.keys

# creating archive (tar.gz)
echo "Creating archive..."
sudo tar czf $now.tar.gz $backupName

# copying archive to destination device
echo "Enter the connection information:"
read -p "Host: " destIP
read -p "Folder: " destFolder
read -p "User: " destUser

echo "Transmitting backup..."
rsync -ze ssh $now.tar.gz $destUser@$destIP:$destFolder

echo "Cleaning up..."
sudo rm -R $backupName
sudo rm $now.tar.gz

echo "Finished!"

# recover:
#   sudo apt-key add ~/repo.keys
#   sudo cp ~/sources.list /etc/apt/sources.list
#   sudo apt-get update
#   sudo apt-get install dselect
#   sudo dpkg --set-selections < ~/package.list
#   sudo dselect
