package bronuh.shit.proxy.server;

import bronuh.shit.metrics.PlayersRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class Events {

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) e.getEntity();

            PlayersRegistry.register(player);
        }
    }
}
