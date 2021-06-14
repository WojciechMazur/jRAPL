#!/bin/bash

function usage() {
	echo "usage: $1 <benchmark> <monitoringEnergy> <iterations> <warmups> <monitorType> <result directory name>"
	echo "  monitorType = [java|c-linklist|c-dynamicarray]"
	echo "  monitoringEnergy = [true|false]"
	exit 1
}

[ $# != 6 ] && usage $0 $@

sudo -v

mycallback=AsyncMonitorCallback
dacapo_jar="dacapo-evaluation-git+309e1fa.jar"
jRAPL_jar="jRAPL-1.0.jar"
classpath="$dacapo_jar:$jRAPL_jar:."

benchmark=$1
monitoringEnergy=$2
iterations=$3
warmups=$4
monitorType=$5
resultDir=$6

mkdir -p $resultDir

source scripts/benchmark_size_assoc
size=${benchmark_size_assoc[$benchmark]}

echo "@run_dac po.sh@@ @ benchmark -> $benchmark @ @"
echo " r n_daca  . h @@  monitoringEnergy -> $monitoringEnergy @.@"
echo "@run_da apo.sh@ @  iterations -> $iterations @ @"
echo "@r n_daca o.s  @@@ warmups -> $warmups @.@"
echo " run dac po. h@  @ monitorType -> $monitorType @ @"
echo "@r n_d capo. h@@ @ resultDir -> $resultDir @.@"
echo "@run_dacapo.sh@@@@ size -> $size @ @"

sudo java -DmonitoringEnergy=$monitoringEnergy -Dwarmups=$warmups \
			-DmonitorType=$monitorType -DresultDir=$resultDir \
			-cp $classpath Harness \
			$benchmark -c $mycallback -n $iterations \
			-s $size

