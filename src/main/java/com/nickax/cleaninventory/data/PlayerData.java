package com.nickax.cleaninventory.data;

import com.nickax.cleaninventory.item.Item;
import com.nickax.genten.language.LanguageIdentifiable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData implements LanguageIdentifiable {

    private final UUID id;
    private final List<Item> blackListedItems;
    private String languageId;

    public PlayerData(Player player) {
        this.id = player.getUniqueId();
        this.blackListedItems = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public boolean containsBlackListedItem(Item item) {
        return blackListedItems.contains(item);
    }

    public List<Item> getBlackListedItems() {
        return blackListedItems;
    }

    public void addBlackListedItem(Item item) {
        if (!blackListedItems.contains(item)) {
            blackListedItems.add(item);
        }
    }

    public void removeBlackListedItem(Item item) {
        blackListedItems.remove(item);
    }

    @Override
    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }
}
