package com.nickax.cleaninventory.command;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.enums.CommandScope;
import com.nickax.genten.command.model.CommandProperties;
import com.nickax.genten.inventory.BaseInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MenuCommand extends BaseCommand {

    private final BaseInventory itemBlacklistInventory;

    public MenuCommand(CleanInventory plugin, BaseCommand parent) {
        super("menu", createProperties(parent), parent.getMessages());
        this.itemBlacklistInventory = plugin.getItemBlacklistInventory();
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        Player player = (Player) sender;
        itemBlacklistInventory.open(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return List.of();
    }

    private static CommandProperties createProperties(BaseCommand parent) {
        return CommandProperties.builder()
                .setParent(parent)
                .setScope(CommandScope.PLAYER)
                .setDescription("open the item blacklist menu")
                .setPermission("cleaninventory.menu")
                .build();
    }
}
