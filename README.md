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

To install this utility on your EV3 Brick, it is necessary to execute
the following steps:

*Brick side*

```
mkdir batteryMonitor
```  

*Host side*

``` bash
git clone https://github.com/ev3dev-lang-java/batteryMonitor.git
./gradlew clean fatjar
scp ./build/libs/batteryMonitor-all-0.1.0-SNAPSHOT.jar robot@192168.1.85:/home/robot/batteryMonitor
scp ./scripts/logback.xml robot@192168.1.85:/home/robot/batteryMonitor
scp ./scripts/stop.sh robot@192168.1.85:/home/robot/batteryMonitor
scp ./scripts/start.sh robot@192168.1.85:/home/robot/batteryMonitor
scp ./scripts/batteryMonitor-service.sh robot@192168.1.85:/etc/init.d
robot@ev3dev:~$ java -server -jar batteryMonitor-all-0.1.0-SNAPSHOT.jar
```

*Brick side*

```
cd batteryMonitor
chmod +x ./start.sh
chmod +x ./stop.sh
cd /etc/init.d
sudo chmod +x batteryMonitor-service.sh
sudo update-rc.d batteryMonitor-service.sh defaults
sudo reboot now
``` 

If now you reboot the system, you should see the process running:

```
robot@ev3dev:~/batteryMonitor$ ps aux | grep java
root       298 10.3 29.0  87548 17024 ?        Sl   22:51   0:32 java -server -Dlogback.configurationFile=/home/robot/batteryMonitor/logback.xml -jar /home/robot/batteryMonitor/batteryMonitor-all-0.1.0-SNAPSHOT.jar
robot      543  0.0  2.2   2620  1300 pts/0    S+   22:56   0:00 grep java
```

# Customize logging

EV3Dev-lang-java uses Logback as the default logging system and 
the project includes a default configuration. 

If you need to execute the project with another configuration the way 
to do it, it is simple. Create a new logback.xml file:

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds">

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </Pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>/home/robot/batteryMonitor/logs/log-${byDay}.log </file>
        <append>true</append>
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
    </appender>

    <appender name="STASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>192.168.1.100:5000</destination>

        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <mdc/>
                <context/>
                <version/>
                <logLevel/>
                <loggerName/>

                <pattern>
                    <pattern>
                        {
                            "appName": "elk-ev3-batteryMonitor",
                            "appVersion": "0.1.0"
                        }
                    </pattern>
                </pattern>

                <threadName/>
                <message/>
                <logstashMarkers/>
                <arguments/>
                <stackTrace/>
            </providers>
        </encoder>
    </appender>

    <logger name="ch.qos.logback.core" level="OFF" />
    <logger name="ev3dev.hardware" level="OFF" />
    <logger name="ev3dev.utils" level="OFF" />
    <logger name="ev3dev.utils.monitor" level="TRACE" />

    <root level="TRACE">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="STASH"/>
    </root>

</configuration>
```

and later execute the jar with the following parameter:

```
robot@ev3dev:~$ java -server -Dlogback.configurationFile=/home/robot/batteryMonitor/logback.xml -jar batteryMonitor-all-0.1.0-SNAPSHOT.jar
```

Further information about Logback configuration here:

https://logback.qos.ch/manual/configuration.html

## Logs

```
tail -f -n10 ./logs/log-20170422T205431.log
2017-04-22 21:04:26 [main] INFO  e.utils.monitor.BatteryMonitorApp - Battery Monitor starting
2017-04-22 21:04:39 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8305.9, Threshold: 8000
2017-04-22 21:05:40 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8306.0, Threshold: 8000
2017-04-22 21:06:41 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8305.8, Threshold: 8000
2017-04-22 21:07:41 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8305.9, Threshold: 8000
2017-04-22 21:08:41 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8305.9, Threshold: 8000
2017-04-22 21:09:41 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8305.5, Threshold: 8000
2017-04-22 21:10:41 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8228.0, Threshold: 8000
2017-04-22 21:11:41 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8218.9, Threshold: 8000
2017-04-22 21:12:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8217.9, Threshold: 8000
2017-04-22 21:13:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8209.8, Threshold: 8000
2017-04-22 21:14:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8207.8, Threshold: 8000
2017-04-22 21:15:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8207.8, Threshold: 8000
2017-04-22 21:16:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8197.7, Threshold: 8000
2017-04-22 21:17:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8198.2, Threshold: 8000
2017-04-22 21:18:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8190.0, Threshold: 8000
2017-04-22 21:19:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8187.9, Threshold: 8000
2017-04-22 21:20:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8187.9, Threshold: 8000
2017-04-22 21:21:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8180.7, Threshold: 8000
2017-04-22 21:22:42 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8179.8, Threshold: 8000
2017-04-22 21:23:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8179.9, Threshold: 8000
2017-04-22 21:24:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8170.9, Threshold: 8000
2017-04-22 21:25:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8169.9, Threshold: 8000
2017-04-22 21:26:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8169.9, Threshold: 8000
2017-04-22 21:27:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8160.9, Threshold: 8000
2017-04-22 21:28:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8159.9, Threshold: 8000
2017-04-22 21:29:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8159.8, Threshold: 8000
2017-04-22 21:30:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8150.9, Threshold: 8000
2017-04-22 21:31:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8149.8, Threshold: 8000
2017-04-22 21:32:43 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8149.9, Threshold: 8000
2017-04-22 21:33:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8142.7, Threshold: 8000
2017-04-22 21:34:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8133.9, Threshold: 8000
2017-04-22 21:35:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8130.0, Threshold: 8000
2017-04-22 21:36:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8129.7, Threshold: 8000
2017-04-22 21:37:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8129.0, Threshold: 8000
2017-04-22 21:38:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8120.9, Threshold: 8000
2017-04-22 21:39:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8119.9, Threshold: 8000
2017-04-22 21:40:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8119.9, Threshold: 8000
2017-04-22 21:41:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8110.9, Threshold: 8000
2017-04-22 21:42:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8110.9, Threshold: 8000
2017-04-22 21:43:44 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8109.0, Threshold: 8000
2017-04-22 21:44:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8105.8, Threshold: 8000
2017-04-22 21:45:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8102.7, Threshold: 8000
2017-04-22 21:46:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8101.9, Threshold: 8000
2017-04-22 21:47:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8101.9, Threshold: 8000
2017-04-22 21:48:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8092.9, Threshold: 8000
2017-04-22 21:49:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8091.9, Threshold: 8000
2017-04-22 21:50:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8091.9, Threshold: 8000
2017-04-22 21:51:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8082.9, Threshold: 8000
2017-04-22 21:52:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8083.8, Threshold: 8000
2017-04-22 21:53:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8081.9, Threshold: 8000
2017-04-22 21:54:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8072.9, Threshold: 8000
2017-04-22 21:55:45 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8073.8, Threshold: 8000
2017-04-22 21:56:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8071.9, Threshold: 8000
2017-04-22 21:57:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8071.8, Threshold: 8000
2017-04-22 21:58:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8062.9, Threshold: 8000
2017-04-22 21:59:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8061.8, Threshold: 8000
2017-04-22 22:00:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8061.9, Threshold: 8000
2017-04-22 22:01:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8052.9, Threshold: 8000
2017-04-22 22:02:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8051.9, Threshold: 8000
2017-04-22 22:03:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8051.0, Threshold: 8000
2017-04-22 22:04:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8046.8, Threshold: 8000
2017-04-22 22:05:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8044.5, Threshold: 8000
2017-04-22 22:06:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8041.7, Threshold: 8000
2017-04-22 22:07:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8041.6, Threshold: 8000
2017-04-22 22:08:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8032.9, Threshold: 8000
2017-04-22 22:09:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8031.8, Threshold: 8000
2017-04-22 22:10:46 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8031.8, Threshold: 8000
2017-04-22 22:11:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8022.0, Threshold: 8000
2017-04-22 22:12:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8021.9, Threshold: 8000
2017-04-22 22:13:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8021.7, Threshold: 8000
2017-04-22 22:14:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8017.8, Threshold: 8000
2017-04-22 22:15:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8015.4, Threshold: 8000
2017-04-22 22:16:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8014.0, Threshold: 8000
2017-04-22 22:17:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8014.0, Threshold: 8000
2017-04-22 22:18:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8003.9, Threshold: 8000
2017-04-22 22:19:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8004.0, Threshold: 8000
2017-04-22 22:20:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 8003.9, Threshold: 8000
2017-04-22 22:21:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Current voltage: 7994.9, Threshold: 8000
2017-04-22 22:21:47 [pool-1-thread-1] DEBUG ev3dev.utils.monitor.BatteryTask - Reached battery threshold
2017-04-22 22:21:47 [main] DEBUG ev3dev.utils.monitor.BatteryExecutor - Thread Pool Shutdown
2017-04-22 22:21:52 [main] DEBUG ev3dev.utils.monitor.BatteryExecutor - Executing action: sudo shutdown now
```


