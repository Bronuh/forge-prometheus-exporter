package bronuh.shit.metrics;

import io.prometheus.client.Collector;
import net.minecraft.entity.player.EntityPlayerMP;
import io.prometheus.client.Gauge;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayersRegistry extends Metric{

    private static ArrayList<PlayerStats> playerStats = new ArrayList<>();
    private static ArrayList<PlayersRegistry> instances = new ArrayList<>();

    private static final Gauge PLAYER_STATS = Gauge.build()
            .name(prefix("player_statistic"))
            .help("Player statistics")
            .labelNames("player_name", "player_uid", "statistic")
            .create();


    public PlayersRegistry() {
        super(PLAYER_STATS);
        instances.add(this);
    }

    public static PlayerStats register(EntityPlayerMP player){
        PlayerStats found = playerStats.stream().filter(s -> (s.playerName.equals(player.getName()))).findFirst().orElse(null);
        PlayerStats stats;
        //player.sendMessage(new TextComponentString(player.getName()+" registering"));
        if(found==null){
            stats = new PlayerStats(player);
            playerStats.add(stats);
            //player.sendMessage(new TextComponentString(player.getName()+" not found, creating"));
        }else{
            //player.sendMessage(new TextComponentString(player.getName()+" found, updating"));
            stats = found.update(player);
        }
        for(PlayersRegistry registry : instances){
            registry.doCollect();
        }

        return stats;
    }


    public static void SaveAll(){
        // To be fucked with
    }

    public static void LoadAll(){
        // To be fucked with
    }


    @Override
    public void doCollect() {
        for(PlayerStats stat : playerStats){
            collect(stat);
        }
    }

    private void collect(PlayerStats statistics){

        for(Stat stat : statistics.getAll()){
            PLAYER_STATS.labels(statistics.playerName,
                    statistics.playerUUID.toString(),
                    PlayerStats.getEnumName(stat)).set(stat.value);
        }
    }

}
