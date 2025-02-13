package com.nickax.cleaninventory.command;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandHelp;
import com.nickax.genten.command.CommandProperties;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CleanInventoryCommand extends BaseCommand {

    public CleanInventoryCommand(CleanInventory plugin) {
        super("cleaninventory", createProperties());
        addSubCommand(new ReloadCommand(plugin, this));
        addSubCommand(new MenuCommand(plugin, this));
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        CommandHelp help = getHelp(sender);
        help.display();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return List.of();
    }

    private static CommandProperties createProperties() {
        return CommandProperties.builder()
                .setAliases(List.of("ci"))
                .build();
    }
}
