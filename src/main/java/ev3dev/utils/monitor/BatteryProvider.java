package ev3dev.utils.monitor;

import ev3dev.sensors.Battery;
import lejos.hardware.Power;
import lombok.extern.slf4j.Slf4j;

public class BatteryProvider {

    private final Power power;

    public BatteryProvider(){
        power = Battery.getInstance();
    }

    public float getVoltageMilliVolt(){
        return power.getVoltageMilliVolt();
    }
}
