package net.ilexiconn.llibrary;

import net.ilexiconn.llibrary.server.ServerProxy;
import net.ilexiconn.llibrary.server.command.Command;
import net.ilexiconn.llibrary.server.command.CommandHandler;
import net.ilexiconn.llibrary.server.command.argument.ArgumentTypes;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = "llibrary", name = "LLibrary", version = LLibrary.VERSION)
public class LLibrary {
    public static final String VERSION = "1.0.0-develop";

    @SidedProxy(serverSide = "net.ilexiconn.llibrary.server.ServerProxy", clientSide = "net.ilexiconn.llibrary.client.ClientProxy")
    public static ServerProxy PROXY;
    @Mod.Instance("llibrary")
    public static LLibrary INSTANCE;
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        LLibrary.NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("llibrary");
        LLibrary.PROXY.onPreInit();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        LLibrary.PROXY.onInit();
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        LLibrary.PROXY.onPostInit();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        Command testCommand = Command.create("test").addRequiredArgument("player", ArgumentTypes.PLAYER).addOptionalArgument("boolean", ArgumentTypes.BOOLEAN);
        CommandHandler.INSTANCE.registerCommand(event, testCommand, (server, sender, arguments) -> {
            sender.addChatMessage(new TextComponentString(arguments.getPlayer("player").getDisplayNameString()));
            if (arguments.hasArgument("boolean")) {
                sender.addChatMessage(new TextComponentString(arguments.getBoolean("boolean") + ""));
            }
        });
    }
}
