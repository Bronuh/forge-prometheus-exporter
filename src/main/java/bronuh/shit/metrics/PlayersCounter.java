package bronuh.shit.metrics;

import bronuh.shit.metrics.Metric;
import bronuh.shit.proxy.CommonProxy;
import io.prometheus.client.Collector;
import io.prometheus.client.Gauge;


public class PlayersCounter extends Metric {

    private static final Gauge PLAYERS = Gauge.build()
            .name(prefix("players"))
            .help("Current online players count")
            .create();

    public PlayersCounter() {
        super(PLAYERS);
    }

    @Override
    public void doCollect() {
        PLAYERS.set(getValue());
    }

    @Override
    public double getValue() {
        return CommonProxy.server.getOnlinePlayerNames().length;
    }
}
