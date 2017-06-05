#!/bin/bash

EV3="EV3"
BRICKPI="BRICKPI"
PISTORMS="PISTORMS"
UNKNOWN="UNKNOWN"
PLATFORM=$UNKNOWN

#1. Detect platform
if [ -d "/sys/class/power_supply/legoev3-battery" ]; then
  echo "The user has a EV3 Brick"
  PLATFORM=$EV3
elif [ -d "/sys/class/power_supply/brickpi-battery" ]; then
  echo "The user has a BrickPi+"
  PLATFORM=$BRICKPI
elif [ -d "/sys/class/power_supply/pistorms-battery" ]; then
  echo "The user has a PiStorms"
  PLATFORM=$PISTORMS
fi
echo "Platform detected: $PLATFORM"
echo
