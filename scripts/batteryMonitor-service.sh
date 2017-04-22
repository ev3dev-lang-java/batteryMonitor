#!/bin/bash
# Battery Monitor
#
# description: A Java battery monitor to protect EV3Dev hardware

case $1 in
    start)
        /bin/bash /home/robot/batteryMonitor/start.sh
    ;;
    stop)
        /bin/bash /home/robot/batteryMonitor/stop.sh
    ;;
    restart)
        /bin/bash /home/robot/batteryMonitor/stop.sh
        /bin/bash /home/robot/batteryMonitor/start.sh
    ;;
esac
exit 0