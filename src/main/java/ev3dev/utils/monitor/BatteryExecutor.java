package ev3dev.utils.monitor;

import ev3dev.utils.Shell;
import org.slf4j.Logger;

import java.util.concurrent.*;

public class BatteryExecutor {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BatteryMonitorApp.class);

    private final String COMMAND_SHUTDOWN = "sudo shutdown now";

    final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void init() throws ExecutionException, InterruptedException {

        boolean executionFlag = false;
        final Future<Boolean> future = executorService.submit(new BatteryTask());
        try {
            executionFlag = future.get();
        } catch (ExecutionException e) {
            log.error(e.getLocalizedMessage());
        }

        threadPoolShutdown();
        if(executionFlag){
            action();
        }
    }

    private void threadPoolShutdown() throws InterruptedException {
        log.debug("Thread Pool Shutdown");
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        executorService.shutdownNow();
    }

    private void action(){
        log.debug("Executing action: {}", COMMAND_SHUTDOWN);
        Shell.execute(COMMAND_SHUTDOWN);
    }

}
