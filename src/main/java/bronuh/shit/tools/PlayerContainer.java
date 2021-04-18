package bronuh.shit.tools;

import bronuh.shit.metrics.PlayerStats;
import bronuh.shit.metrics.Stat;

import java.util.HashMap;

public class PlayerContainer {
    public String name;
    public String UUID;

    public HashMap<String, Double> stats = new HashMap<>();

    public PlayerContainer(){}

    public PlayerContainer(PlayerStats playerStats){
        this.name = playerStats.playerName;
        this.UUID = playerStats.playerUUID.toString();

        for(Stat stat : playerStats.getAll()){
            stats.put(PlayerStats.getEnumName(stat), stat.value);
        }
    }

    public PlayerContainer(String name, String UUID, PlayerStats playerStats){
        this.name = name;
        this.UUID = UUID;

        for(Stat stat : playerStats.getAll()){
            stats.put(stat.statId, stat.value);
        }
    }



}
