#!/bin/bash
cd /home/

# variables
backupName="backup"
packageLists="packageLists"

# current time
userName="pi"
now="$(date +'%Y-%m-%d_%H-%M-%S')"
echo "Backup process stared: (timestamp: $now):"

# create backup folder
echo "Copying contents..."
mkdir $backupName

# copying files
cp -r /etc $backupName
cp -r $userName $backupName

# creating and copying package list
mkdir $backupName/$packageLists
dpkg --get-selections > $backupName/$packageLists/package.list
cp /etc/apt/sources.list $backupName/$packageLists/sources.list
apt-key exportall > $backupName/$packageLists/repo.keys

# creating archive (tar.gz)
echo "Creating archive..."
tar czf $now.tar.gz $backupName

# copying archive to destination device
echo "Enter the connection information:"
read -p "IP: " destIP
read -p "User:" destUser
read -p "Folder:" destFolder

rsync -ze ssh $now.tar.gz $destUser@$destIP:$destFolder
echo "Transmitting backup..."

# tidy up
rm -R $backupName
rm $now.tar.gz
echo "finished"

# recover:
#   sudo apt-key add ~/repo.keys
#   sudo cp ~/sources.list /etc/apt/sources.list
#   sudo apt-get update
#   sudo apt-get install dselect
#   sudo dpkg --set-selections < ~/package.list
#   sudo dselect
