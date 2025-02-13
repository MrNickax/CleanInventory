package com.nickax.cleaninventory.data;

import com.nickax.cleaninventory.item.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData {

    private final UUID id;
    private final List<Item> blackListedItems;

    public PlayerData(Player player) {
        this.id = player.getUniqueId();
        this.blackListedItems = new ArrayList<>();
    }

    public boolean containsBlackListedItem(Item item) {
        return blackListedItems.contains(item);
    }

    public void addBlackListedItem(Item item) {
        if (!blackListedItems.contains(item)) {
            blackListedItems.add(item);
        }
    }

    public void removeBlackListedItem(Item item) {
        blackListedItems.remove(item);
    }

    public UUID getId() {
        return id;
    }

    public List<Item> getBlackListedItems() {
        return blackListedItems;
    }
}
