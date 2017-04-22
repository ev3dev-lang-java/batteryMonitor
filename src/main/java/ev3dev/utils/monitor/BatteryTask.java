package ev3dev.utils.monitor;

import lombok.extern.slf4j.Slf4j;
import ev3dev.sensors.Battery;
import lejos.hardware.Power;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public @Slf4j class BatteryTask implements Callable<Boolean> {

    private static int DEFAULT_VOLTAGE_THRESHOLD = 7500000;
    private static int DEFAULT_VOLTAGE_MEASURE_INTERVAL = 1000;
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
        int counter = 0;

        while(voltageFlag) {

            currentVoltage = this.getVoltageAverage();
            log.debug("Current voltage: {}, Threshold: {}", currentVoltage, threshold);

            if (currentVoltage < threshold) {
                break;
            }

            counter++;
            if(counter > 10){
                break;
            }

            Thread.sleep(DEFAULT_VOLTAGE_MEASURE_INTERVAL);
        }
        log.debug("Returning the control to main Thread");
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
            voltageList.add(Double.valueOf(battery.getVoltage()));
        }
        return voltageList;
    }
}
