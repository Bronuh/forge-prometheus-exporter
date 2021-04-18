package bronuh.shit.metrics;

import bronuh.shit.proxy.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;

public class MetricsCommand extends CommandBase {

    @Override
    public String getName() {
        return "metrics";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "Try to not fuck up!";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        sender.sendMessage(new TextComponentString("======================================"));
        sender.sendMessage(new TextComponentString("Average TPS: "+ Math.round(CommonProxy.tpsMetric.getValue()*10)/10.0));
        sender.sendMessage(new TextComponentString("Players online: "+ (int)CommonProxy.playersCountMetric.getValue()));
        sender.sendMessage(new TextComponentString("Allocated memory: "+ (CommonProxy.memoryMetric.getValue()
        /Math.round(Math.pow(1024,2)))+" Mb"));
        sender.sendMessage(new TextComponentString("Loaded chunks: "+ (int)CommonProxy.chunksMetric.getValue()));
    }
}
