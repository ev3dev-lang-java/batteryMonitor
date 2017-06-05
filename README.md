# batteryMonitor
A Java Battery monitor for EV3Dev hardware.

# User Story

``` gherkin
Feature: Develop a Java Battery Monitor process to protect Hardware.
  
Scenario: EV3Dev Startup
Given A EV3Dev platform (EV3, Brickpi & PiStorms)
When The user turn on the Brick 
Then A Java process will start monitoring the battery

Scenario: Default configuration
Given A EV3Dev platform (EV3, Brickpi & PiStorms)
When The user turn on the Brick 
Then A Java process will start using default configuration. 
The default configuration is: 
- THRESHOLD_VOLTAGE = 7.5v
- LINUX_ACTION = `sudo shutdown now`
```

# Getting Started

To install this utility on your EV3 Brick, BrickPi+ or PiStorms, use the Installer.

https://github.com/ev3dev-lang-java/installer



