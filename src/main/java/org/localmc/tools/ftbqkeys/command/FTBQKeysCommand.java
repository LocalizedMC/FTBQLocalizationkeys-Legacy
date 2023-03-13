package org.localmc.tools.ftbqkeys.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class FTBQKeysCommand extends CommandTreeBase {

    public FTBQKeysCommand() {
        super.addSubcommand(new FTBQKeysConvCommand());
    }

    @Override
    public String getName() {
        return "ftbqkey";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.ftbqkeys.usage";
    }
}
