package bronuh.shit.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.Gauge;

public class Memory extends Metric{

    private static final Gauge MEMORY = Gauge.build()
            .name(prefix("jvm_memory"))
            .help("JVM memory usage")
            .labelNames("type")
            .create();


    public Memory() {
        super(MEMORY);
    }

    @Override
    public void doCollect() {
        MEMORY.labels("max").set(Runtime.getRuntime().maxMemory());
        MEMORY.labels("free").set(Runtime.getRuntime().freeMemory());
        MEMORY.labels("allocated").set(Runtime.getRuntime().totalMemory());
    }
}
