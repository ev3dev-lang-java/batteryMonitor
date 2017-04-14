import ev3dev.utils.battery.BatteryMonitor;
import ev3dev.utils.battery.Monitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class BatteryMonitorMocksTest {

    @Mock
    private Monitor monitor;

    @Before
    public void doBeforeEachTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        monitor = new Monitor(1000000000);
    }

    @Test
    public void test() throws Exception {

        BatteryMonitor batteryMonitor = new BatteryMonitor();

        final List<Double> voltageList = new ArrayList<>();
        voltageList.add(10000000d);
        given(monitor.getVoltages()).willReturn(voltageList);

        batteryMonitor.init();

        verify(monitor).getVoltages();
    }

}
