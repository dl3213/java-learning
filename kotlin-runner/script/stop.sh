#!/bin/bash
PID=$(ps -ef | grep kotlin-runner-0.0.1-SNAPSHOT | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then echo app is already stopped
else kill -15 $PID
fi