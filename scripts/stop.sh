#!/bin/bash

pid=`ps aux | grep batteryMonitor | awk '{print $2}'`
kill -9 $pid
