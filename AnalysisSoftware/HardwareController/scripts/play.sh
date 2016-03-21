#!/bin/bash

if [[ "$1" == *wav ]]; then
	aplay -q $1
else
	mpg123 -q $1
fi
