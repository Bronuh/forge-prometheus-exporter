package bronuh.shit.metrics;

import bronuh.shit.metrics.tools.Scheduler;
import bronuh.shit.metrics.tools.TpsCollector;
import io.prometheus.client.Collector;
import io.prometheus.client.Gauge;

public class Tps extends Metric {

    private static final Gauge TPS = Gauge.build()
            .name(prefix("tps"))
            .help("Server TPS (ticks per second)")
            .create();

    private long taskId;

    private TpsCollector tpsCollector = new TpsCollector();

    public Tps() {
        super(TPS);
    }


    @Override
    public void enable() {
        super.enable();
        this.taskId = startTask();
    }

    @Override
    public void disable() {
        super.disable();
        Scheduler.cancelTask(taskId);
    }

    private long startTask() {
        return Scheduler
                .scheduleSyncRepeatingTask(tpsCollector, 0, TpsCollector.POLL_INTERVAL);
    }

    @Override
    public void doCollect() {
        TPS.set(tpsCollector.getAverageTPS());
    }
}