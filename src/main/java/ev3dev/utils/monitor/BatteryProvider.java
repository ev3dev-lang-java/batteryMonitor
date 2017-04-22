package ev3dev.utils.monitor;

import ev3dev.sensors.Battery;
import lejos.hardware.Power;

public class BatteryProvider {

    private Power power;

    public BatteryProvider(){
        power = Battery.getInstance();
    }

    public float getVoltage(){
        return power.getVoltage();
    }
}
