package bronuh.shit.metrics;

import bronuh.shit.proxy.CommonProxy;
import io.prometheus.client.Gauge;
import net.minecraft.world.WorldServer;

public class Chunks extends Metric {
    private static final Gauge CHUNKS = Gauge.build()
            .name(prefix("chunks"))
            .help("Loaded chunks")
            .create();

    public Chunks() {
        super(CHUNKS);
    }

    @Override
    public void doCollect() {
        CHUNKS.set(getValue());
    }

    @Override
    public double getValue() {
        WorldServer[] worlds = CommonProxy.server.worlds;
        int sum = 0;
        for(WorldServer world : worlds){
            sum += world.getChunkProvider().getLoadedChunkCount();
        }

        //System.out.println("Loaded chunks: "+sum);
        return sum;
    }
}
