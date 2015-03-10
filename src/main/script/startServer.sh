#!/bin/bash

set -e

nom=`basename $0`
echo "+"
echo "+ +  usage : ${nom} <port>"
echo "+ +"      

java -jar lib/subscriber-2.0.jar  $1
