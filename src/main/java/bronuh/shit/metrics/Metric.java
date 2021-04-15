package bronuh.shit.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

import java.util.ArrayList;

public abstract class Metric {

    private final static String COMMON_PREFIX = "mc_";

    private final Collector collector;
    private boolean enabled = false;
    private static ArrayList<Metric> metrics = new ArrayList<>();


    protected Metric(Collector collector) {
        this.collector = collector;
    }

    public static Metric register(Metric metric){
        metrics.add(metric);
        metric.enable();
        return metric;
    }

    public static void collectMetrics() {
        metrics.forEach(Metric::collect);
    }

    public void collect() {

        if (!enabled) {
            return;
        }

        try {
            doCollect();
        } catch (Exception e) {
            logException(e);
        }
    }

    public abstract void doCollect();

    private void logException(Exception e) {
        final String className = getClass().getSimpleName();
        System.out.println("Shit happens ["+className+"]: "+e.getMessage());
    }



    protected static String prefix(String name) {
        return COMMON_PREFIX + name;
    }

    public void enable() {
        CollectorRegistry.defaultRegistry.register(collector);
        enabled = true;
    }

    public void disable() {
        CollectorRegistry.defaultRegistry.unregister(collector);
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
