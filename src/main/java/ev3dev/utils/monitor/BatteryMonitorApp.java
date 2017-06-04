package ev3dev.utils.monitor;

import org.slf4j.Logger;

public class BatteryMonitorApp {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BatteryMonitorApp.class);

    //TODO Add support to send parameters by command line
    public static void main(final String... args) throws Exception {

        log.info("Battery Monitor starting");
        BatteryExecutor batteryExecutor = new BatteryExecutor();
        batteryExecutor.init();
        log.info("Battery Monitor finishing");
    }

}
