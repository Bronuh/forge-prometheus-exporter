package bronuh.shit;
import bronuh.shit.proxy.CommonProxy;
import bronuh.shit.tools.MetricsContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = "prometheusexporter",acceptableRemoteVersions = "*")
public class Shit {



    @SidedProxy(clientSide = "bronuh.shit.proxy.ClientProxy", serverSide = "bronuh.shit.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        proxy.serverStarting(event);//Вызов из CommonProxy в главном классе
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        proxy.loadComplete(event);
    }

    @Mod.EventHandler
    public void stop(FMLServerStoppingEvent event){
        MetricsContainer.Save();
    }


}