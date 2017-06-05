#!/bin/bash

source "platform.sh"

if [ "$PLATFORM" == "$EV3" ]; then
    echo "The memory usage is minimum"
elif [ "$PLATFORM" == "$BRICKPI" ]; then
    pid=`ps aux | grep batteryMonitor | awk '{print $2}'`
    kill -9 $pid
elif [ "$PLATFORM" == "$PISTORMS" ]; then
    pid=`ps aux | grep batteryMonitor | awk '{print $2}'`
    kill -9 $pid
fi

