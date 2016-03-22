package net.ilexiconn.llibrary.test;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.ilexiconn.llibrary.server.command.Command;
import net.ilexiconn.llibrary.server.command.CommandHandler;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "CommandTest")
public class CommandTest {
    public static final Logger LOGGER = LogManager.getLogger("CommandTest");

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        Command command = Command.create("testcommand")
                .addRequiredArgument("required_player", EntityPlayer.class)
                .addRequiredArgument("required_double", double.class)
                .addOptionalArgument("optional_integer", int.class);

        CommandHandler.INSTANCE.registerCommand(event, command, (server, sender, arguments) -> {
            CommandTest.LOGGER.info("=== Test command ===");
            CommandTest.LOGGER.info("== Player ==");
            EntityPlayer required_player = arguments.getArgument("required_player");
            CommandTest.LOGGER.info(" -> " + required_player.getUniqueID());
            CommandTest.LOGGER.info("== Double ==");
            double required_double = arguments.getArgument("required_double");
            CommandTest.LOGGER.info(" -> " + required_double);
            CommandTest.LOGGER.info("== Integer ==");
            boolean has_optional_integer = arguments.hasArgument("optional_integer");
            CommandTest.LOGGER.info("hasArgument: " + has_optional_integer);
            if (has_optional_integer) {
                int optional_integer = arguments.getArgument("optional_integer");
                CommandTest.LOGGER.info(" -> " + optional_integer);
            }
        });
    }
}
