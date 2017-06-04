package ev3dev.utils.monitor;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class BatteryTask implements Callable<Boolean> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BatteryTask.class);

    private static int DEFAULT_VOLTAGE_THRESHOLD = 7500;
    private static int DEFAULT_VOLTAGE_MEASURE_INTERVAL = 1;
    private static final int DEFAULT_MEASURES = 10;

    final BatteryProvider battery;

    private int threshold;

    public BatteryTask(){
        this.threshold = DEFAULT_VOLTAGE_THRESHOLD;
        battery = new BatteryProvider();
    }

    @Override
    public Boolean call() throws InterruptedException {

        boolean voltageFlag = true;
        double currentVoltage;

        while(voltageFlag) {

            currentVoltage = this.getVoltageAverage();
            log.debug("Current voltage: {}, Threshold: {}", currentVoltage, threshold);

            if (currentVoltage < threshold) {
                break;
            }

            TimeUnit.MINUTES.sleep(DEFAULT_VOLTAGE_MEASURE_INTERVAL);
        }
        log.debug("Reached battery threshold");
        return true;
    }

    private double getVoltageAverage(){
        return this.getVoltages()
            .stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .getAsDouble();
    }

    //TODO Manage how to manage EV3Dev exception
    private List<Double> getVoltages(){
        final List<Double> voltageList = new ArrayList<>();
        for(int x = 0; x < DEFAULT_MEASURES; x++){
            voltageList.add(Double.valueOf(battery.getVoltageMilliVolt()));
        }
        return voltageList;
    }
}
