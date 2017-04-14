package ev3dev.utils.battery;

import ev3dev.utils.Shell;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

public @Slf4j class BatteryMonitor {

    private static int DEFAULT_VOLTAGE_THRESHOLD = 7500000;
    private static String SHUTDOWN_COMMAND = "sudo shutdown now";

    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    final Future<Double> callFuture;

    public BatteryMonitor(final Monitor monitor){
        callFuture = executorService.submit(monitor);
    }

    public boolean init() throws ExecutionException, InterruptedException {

        final Double voltage = callFuture.get();

        log.info("Current voltage:" + voltage);
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        executorService.shutdownNow();
        //Shell.execute(SHUTDOWN_COMMAND);

        return true;
    }

    //TODO Add support to send parameters by command line
    public static void main(final String... args) throws ExecutionException, InterruptedException {

        Monitor monitor = new Monitor(DEFAULT_VOLTAGE_THRESHOLD);
        BatteryMonitor batteryMonitor = new BatteryMonitor(monitor);
        batteryMonitor.init();
    }

}
