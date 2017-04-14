package ev3dev.utils.battery;

import ev3dev.sensors.Battery;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public @Slf4j class Monitor implements Callable<Double> {

    private static final int DEFAULT_MEASURES = 10;
    private final int threshold;

    public Monitor(final int threshold){
        this.threshold = threshold;
    }

    @Override
    public Double call() throws InterruptedException {

        boolean voltageFlag = true;
        double currentVoltage = 0;

        while(voltageFlag) {
            currentVoltage = this.getVoltages()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .getAsDouble();
            log.trace("Current voltage: {}, Threshold: {}", currentVoltage, threshold);

            if (currentVoltage < threshold) {
                break;
            }
        }
        return currentVoltage;
    }

    private List<Double> getVoltages(){
        final List<Double> voltageList = new ArrayList<>();
        for(int x = 0; x < DEFAULT_MEASURES; x++){
            voltageList.add(Double.valueOf(Battery.getInstance().getVoltage()));
        }
        return voltageList;
    }
}
