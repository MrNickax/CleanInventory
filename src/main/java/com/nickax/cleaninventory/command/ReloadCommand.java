package com.nickax.cleaninventory.command;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandProperties;
import com.nickax.genten.util.string.StringUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends BaseCommand {

    private final CleanInventory plugin;

    public ReloadCommand(CleanInventory plugin, BaseCommand parent) {
        super("reload", createProperties(parent));
        this.plugin = plugin;
    }

    @Override
    public boolean onExecute(CommandSender sender, String s, String[] strings) {
        plugin.reload();
        sender.sendMessage(StringUtil.color("&aConfiguration successfully reloaded!"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String s, String[] strings) {
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
