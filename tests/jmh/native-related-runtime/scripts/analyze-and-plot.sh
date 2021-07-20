#!/bin/bash

if [ $1 != 'JavaSide' ] && [ $1 != 'CSide' ] && [ $1 != 'readMSR' ]
then
	echo "invalid option: $1"
	echo "must be JavaSide, CSide, or readMSR"
	exit 2
fi

files=$1_*_histogram.data
python3 scripts/make_histogram.py $files #"$1_*_histogram.data"
