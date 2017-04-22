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
the following steps on your host:

``` bash
git clone https://github.com/ev3dev-lang-java/batteryMonitor.git
./gradlew clean fatjar
scp ./build/libs/batteryMonitor-all-0.1.0-SNAPSHOT.jar robot@192168.1.85:/home/robot/
robot@ev3dev:~$ java -server -jar batteryMonitor-all-0.1.0-SNAPSHOT.jar
```

# Customize logging

EV3Dev-lang-java uses Logback as the default logging system and 
the project includes a default configuration. 

If you need to execute the project with another configuration the way 
to do it, it is simple. Create a new logback.xml file:

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </Pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file> log-${byDay}.log </file>
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
robot@ev3dev:~$ java -server -Dlogback.configurationFile=/home/robot/logback.xml -jar batteryMonitor-all-0.1.0-SNAPSHOT.jar
```

Further information about Logback configuration here:
https://logback.qos.ch/manual/configuration.html