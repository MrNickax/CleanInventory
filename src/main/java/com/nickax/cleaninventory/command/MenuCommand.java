package com.nickax.cleaninventory.command;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.inventory.BlackListInventoryProvider;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.CommandProperties;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MenuCommand extends BaseCommand {

    private final CleanInventory plugin;

    public MenuCommand(CleanInventory plugin, BaseCommand parent) {
        super("menu", createProperties(parent), parent.getMessages());
        this.plugin = plugin;
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        SmartInventory inventory = SmartInventory.builder()
                .id("blacklist")
                .provider(new BlackListInventoryProvider(plugin.getPlayerDataRepository()))
                .manager(plugin.getInventoryManager())
                .title("Item Blacklist")
                .size(5, 9)
                .build();
        inventory.open((Player) sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return List.of();
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder()
                .setParent(parent)
                .setDescription("open the item blacklist menu")
                .setPermission("cleaninventory.menu")
                .build();
    }
}
