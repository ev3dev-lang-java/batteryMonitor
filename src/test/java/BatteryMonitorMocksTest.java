import ev3dev.utils.battery.BatteryMonitor;
import ev3dev.utils.battery.Monitor;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BatteryMonitorMocksTest {

    @Mock
    private Monitor monitor;

    BatteryMonitor batteryMonitor;

    @Test
    public void test() throws Exception {

        final List<Double> voltageList = new ArrayList<>();
        voltageList.add(10000000d);
        given(monitor.getVoltages()).willReturn(voltageList);

        batteryMonitor = new BatteryMonitor(monitor);
        batteryMonitor.init();

        verify(monitor).getVoltages();
    }

}
