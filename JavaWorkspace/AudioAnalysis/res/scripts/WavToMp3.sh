#!/bin/bash

iterate() {
	for file in "$1"/*
	do 
		if [[ -d $file ]]; then
			mkdir "${file/$folderIn/$folderOut}"
			iterate "$file"
		else
			if [[ "$file" == *.wav ]]; then
				dstFile=${file/$folderIn/$folderOut}
				dstFile=${dstFile/.wav/.mp3}
				echo "Processing $file"
				ffmpeg -loglevel error -i "$file" "$dstFile"
			fi
		fi
	done
}

read -p "Input folder: " folderIn
read -p "Output folder: " folderOut

rm -R "$folderOut"
mkdir "$folderOut"

iterate "$folderIn"