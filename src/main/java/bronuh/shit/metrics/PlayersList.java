package bronuh.shit.metrics;

import bronuh.shit.proxy.CommonProxy;
import io.prometheus.client.Gauge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@Mod.EventBusSubscriber(Side.SERVER)
public class PlayersList extends Metric {

    private static final Gauge PLAYERS_LIST = Gauge.build()
            .name(prefix("player_online"))
            .help("Player statistics")
            .labelNames("name")
            .create();

    public PlayersList() {
        super(PLAYERS_LIST);
    }

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent e) {
            EntityPlayer player = (EntityPlayerMP) e.player;
        PLAYERS_LIST.labels(player.getName()).set(0);
    }

    @Override
    public void doCollect() {
        List<EntityPlayerMP> players = CommonProxy.server.getPlayerList().getPlayers();

        for(EntityPlayerMP player : players){
            PLAYERS_LIST.labels(player.getName()).set(1);
        }
    }

    @Override
    public double getValue() {
        return -1;
    }
}
