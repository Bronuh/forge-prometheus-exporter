package bronuh.shit.metrics;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerStats {

    private ArrayList<Stat> stats = new ArrayList<>();

    private static boolean cached = false;
    private static HashMap<String,String> namesCache = new HashMap<>();
    private static String[] customStats = {"stat.mineBlock","stat.useItem"};

    private static StatBase[] statIds = {
            StatList.BOAT_ONE_CM,
            StatList.CROUCH_ONE_CM,
            StatList.WALK_ONE_CM,
            StatList.SPRINT_ONE_CM,
            StatList.DIVE_ONE_CM,
            StatList.DAMAGE_DEALT,
            StatList.DAMAGE_TAKEN,
            StatList.DEATHS,
            StatList.FALL_ONE_CM,
            StatList.FLY_ONE_CM,
            StatList.MOB_KILLS,
            StatList.JUMP,
            StatList.ITEM_ENCHANTED,
            StatList.PLAY_ONE_MINUTE,
            StatList.TRADED_WITH_VILLAGER,
            StatList.CAKE_SLICES_EATEN,
            StatList.FISH_CAUGHT,
            StatList.PLAYER_KILLS,
            StatList.SLEEP_IN_BED
    };

    public String playerName;
    public UUID playerUUID;



    public PlayerStats(EntityPlayerMP player){
        playerName = player.getName();
        playerUUID = player.getUniqueID();
        scanStats(player,statIds);
    }



    public static String getEnumName(Stat stat) {
        if(!cached){
            for(StatBase _stat : StatList.BASIC_STATS){
                namesCache.put(_stat.statId, rename(_stat.statId));
            }
            for(String _stat : customStats){
                namesCache.put(_stat, rename(_stat));
            }
            cached = true;
        }
        return namesCache.get(stat.statId);
    }



    private static String rename(String name){
        String renamed = name.replace("stat.","");
        StringBuffer buffer = new StringBuffer();

        for(int i = 0; i<renamed.length();i++){
            if(Character.isUpperCase(renamed.charAt(i))){
                buffer.append('_');
            }
            buffer.append(renamed.charAt(i));
        }
        renamed = buffer.toString();
        renamed = renamed.toUpperCase();

        return renamed;
    }



    private void scanStats(EntityPlayerMP player, StatBase[] statIds) {
        StatisticsManagerServer statServer = player.getStatFile();
        for(StatBase stat : StatList.BASIC_STATS){
            set(stat.statId,stat.getStatName().getFormattedText(),statServer.readStat(stat));
        }
        int sum = 0;
        for(StatCrafting statBlock : StatList.MINE_BLOCK_STATS){
            sum+=statServer.readStat(statBlock);
        }
        set("stat.mineBlock","Blocks mined",sum);

        sum = 0;
        for(StatCrafting statBlock : StatList.USE_ITEM_STATS){
            sum+=statServer.readStat(statBlock);
        }

        set("stat.useItem","Items used",sum);
    }




    public void set(String statId, String statName, int value){
        if(!exists(statId)){
            stats.add(new Stat(statId,statName,value));
        }else{
            Stat found = find(statId);
            found.statName = statName;
            found.value = value;
        }
    }




    public void set(String statId, int value){
        if(!exists(statId)){
            stats.add(new Stat(statId,statId.toString(),value));
        }else{
            Stat found = find(statId);
            found.value = value;
        }
    }




    public Stat get(String statId){
        return find(statId);
    }

    public List<Stat> getAll(){
        return stats;
    }




    private Stat find(String statId){
        Stat found = stats
                .stream()
                .filter(s -> (s.statId.equals(statId)))
                .findFirst()
                .orElse(null);
        return found;
    }


    private boolean exists(String statId){
        if(find(statId) == null){
            return false;
        }
        return true;
    }


    public PlayerStats update(EntityPlayerMP player) {
        scanStats(player,statIds);
        return this;
    }
}
