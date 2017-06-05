#!/bin/bash

source "/home/robot/batteryMonitor/platform.sh"

if [ "$PLATFORM" == "$EV3" ]; then
    echo "Executing a Script for EV3"
    cd /sys/class/power_supply/legoev3-battery
    while true; do
        voltage=`cat voltage_now`
        echo $voltage
        if   [ $voltage -lt 7500000 ]; then
            break;
        fi
        sleep 1m
    done
    shutdown now
elif [ "$PLATFORM" == "$BRICKPI" ]; then
    java -server -Dlogback.configurationFile=/home/robot/batteryMonitor/logback.xml -jar /home/robot/batteryMonitor/batteryMonitor-all-0.2.0-SNAPSHOT.jar &
elif [ "$PLATFORM" == "$PISTORMS" ]; then
    java -server -Dlogback.configurationFile=/home/robot/batteryMonitor/logback.xml -jar /home/robot/batteryMonitor/batteryMonitor-all-0.2.0-SNAPSHOT.jar &
fi
