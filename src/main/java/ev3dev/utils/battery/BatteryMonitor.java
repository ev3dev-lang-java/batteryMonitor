package ev3dev.utils.battery;

import ev3dev.utils.Shell;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

public @Slf4j class BatteryMonitor {

    private static int DEFAULT_VOLTAGE_THRESHOLD = 7500000;
    private static String SHUTDOWN_COMMAND = "sudo shutdown now";

    //TODO Add support to send parameters by command line
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Future<Double> callFuture = executorService.submit(new Monitor(DEFAULT_VOLTAGE_THRESHOLD));
        final Double voltage = callFuture.get();

        log.info("Current voltage:" + voltage);
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        executorService.shutdownNow();

        Shell.execute(SHUTDOWN_COMMAND);
    }

}
