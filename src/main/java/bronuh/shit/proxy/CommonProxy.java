package bronuh.shit.proxy;

import bronuh.shit.MetricsServer;
import bronuh.shit.metrics.*;
import bronuh.shit.metrics.tools.Scheduler;
import bronuh.shit.proxy.server.Events;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

public class CommonProxy
{
    ArrayList<Metric> metrics = new ArrayList<>();
    public static Logger logger;
    public static MinecraftServer server;
    public static MetricsServer metricsServer;
    public static Configuration config;
    public static final String VERSION = "1.0";

    public static int port;
    public static int collectPerfTickInterval;
    public static int collectPlayersTickInterval;
    public static int collectPlayersStatsTickInterval;

    public static boolean enableTpsMetric;
    public static boolean enableMemoryMetric;
    public static boolean enablePlayersCountMetric;
    public static boolean enablePlayersListMetric;
    public static boolean enablePlayersStatsMetric;
    public static boolean enableLoadedChunksMetric;
    public static Metric playersStatsMetric;
    public static Metric playersCountMetric;
    public static Metric playersListMetric;
    public static Metric memoryMetric;
    public static Metric tpsMetric;
    public static Metric chunksMetric;


    public void preInit(FMLPreInitializationEvent event)
    {

        File configDir = event.getModConfigurationDirectory();
        config = new Configuration(new File(configDir, "/bronuh/config.cfg"), VERSION, true);
        config.load();

        port = config.get("export",
                "Prometheus export port",
                9225).getInt();
        collectPlayersStatsTickInterval = config.get("export",
                "Ticks between collecting players statistics",
                1200).getInt();

        collectPerfTickInterval = config.get("export",
                "Ticks between collecting performance",
                100).getInt();

        collectPlayersTickInterval = config.get("export",
                "Ticks between collecting online players",
                100).getInt();


        enableTpsMetric = config.get("metrics",
                "Collect server TPS metrics",
                true).getBoolean();

        enableMemoryMetric = config.get("metrics",
                "Collect JVM memory usage metrics",
                true).getBoolean();

        enablePlayersCountMetric = config.get("metrics",
                "Collect online players count",
                true).getBoolean();

        enablePlayersListMetric = config.get("metrics",
                "Collect online players names",
                true).getBoolean();

        enablePlayersStatsMetric = config.get("metrics",
                "Collect online players statistics",
                true).getBoolean();

        enableLoadedChunksMetric = config.get("metrics",
                "Collect loaded chunks",
                true).getBoolean();


        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(new Scheduler());
        MinecraftForge.EVENT_BUS.register(new Events());
        MinecraftForge.EVENT_BUS.register(new PlayersList());
    }

    public void init(FMLInitializationEvent event)
    {
        //event.registerServerCommand(new CommandRepair());

    }

    public void postInit(FMLPostInitializationEvent event) {
        server = FMLCommonHandler.instance().getMinecraftServerInstance();

        try {

            if(enablePlayersStatsMetric){
                playersStatsMetric = Metric.register(new PlayersRegistry());
                Scheduler.scheduleSyncRepeatingTask(playersStatsMetric::doCollect, 0, collectPlayersStatsTickInterval,
                        "Collecting players stats");
            }
            if(enablePlayersCountMetric){
                playersCountMetric = Metric.register(new PlayersCounter());
                Scheduler.scheduleSyncRepeatingTask(playersCountMetric::doCollect, 0, collectPlayersTickInterval,
                        "Collecting players count");
            }
            if(enablePlayersListMetric){
                playersListMetric = Metric.register(new PlayersList());
                Scheduler.scheduleSyncRepeatingTask(playersListMetric::doCollect, 0, collectPlayersTickInterval,
                        "Collecting players list");
            }
            if(enableMemoryMetric){
                memoryMetric = Metric.register(new Memory());
                Scheduler.scheduleSyncRepeatingTask(memoryMetric::doCollect, 0, collectPerfTickInterval,
                        "Collecting JVM memory usage");
            }
            if(enableTpsMetric){
                tpsMetric = Metric.register(new Tps());
                Scheduler.scheduleSyncRepeatingTask(tpsMetric::doCollect, 0, collectPerfTickInterval,
                        "Collecting server TPS");
            }
            if(enableLoadedChunksMetric){
                chunksMetric = Metric.register(new Chunks());
                Scheduler.scheduleSyncRepeatingTask(chunksMetric::doCollect, 0, collectPerfTickInterval,
                        "Collecting loaded chunks");
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }

        enableMetrics();
    }


    private void enableMetrics(){
        try {
            metricsServer = new MetricsServer(port);
            metricsServer.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


    public void loadComplete(FMLLoadCompleteEvent event) {
        config.save();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new MetricsCommand());
    }
}
