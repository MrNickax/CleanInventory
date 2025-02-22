package com.nickax.cleaninventory.command;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.model.CommandProperties;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends BaseCommand {

    private final CleanInventory plugin;

    public ReloadCommand(CleanInventory plugin, BaseCommand parent) {
        super("reload", createProperties(parent));
        this.plugin = plugin;
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        plugin.reload();
        plugin.getLanguageAccessor().sendMessage("reload", sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return List.of();
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder()
                .setParent(parent)
                .setDescription("reload the plugin")
                .setPermission("cleaninventory.reload")
                .build();
    }
}
