package ev3dev.utils.monitor;

import lombok.extern.slf4j.Slf4j;

public @Slf4j class BatteryMonitorApp {

    //TODO Add support to send parameters by command line
    public static void main(final String... args) throws Exception {

        log.info("Battery Monitor starting");
        BatteryExecutor batteryExecutor = new BatteryExecutor();
        batteryExecutor.init();
        log.info("Battery Monitor finishing");
    }

}
