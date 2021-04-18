package bronuh.shit.tools;

import bronuh.shit.metrics.PlayerStats;
import bronuh.shit.metrics.PlayersRegistry;
import bronuh.shit.proxy.CommonProxy;

import java.io.IOException;
import java.util.HashMap;

public class MetricsContainer{

    private static MetricsContainer instance = new MetricsContainer();
    public HashMap<String,PlayerContainer> containers = new HashMap<>();


    public MetricsContainer(){
        instance = this;
    }

    public MetricsContainer getInstance(){
        return instance;
    }

    public PlayerContainer put(PlayerStats playerStats){
        PlayerContainer container = new PlayerContainer(playerStats);
        containers.put(container.name, container);
        return container;
    }

    public PlayerContainer get(String name){
        PlayerContainer container = null;
        try{
            container = containers.get(name);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return container;
    }

    public static HashMap<String,PlayerContainer> getAll(){
        return instance.containers;
    }


    public static void Save(){
        for(PlayerStats stats : PlayersRegistry.getAll()){
            instance.put(stats);
        }

        try {
            JsonContainerLoader.saveExternalFile(instance, "Metrics.json");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void Load(){
        CommonProxy.logger.info("> Metrics Container loading...");
        try {
            instance = JsonContainerLoader.loadExternalFile(instance.getClass(), "Metrics.json");
            CommonProxy.logger.info("Metrics loaded successfuly");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
