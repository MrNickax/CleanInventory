package com.nickax.cleaninventory.data;

import com.nickax.genten.item.Item;
import com.nickax.genten.language.LanguageProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerData implements LanguageProvider {

    private final UUID id;
    private final String name;
    private final List<Item> blackListedItems;
    private String languageId;

    public PlayerData(Player player) {
        this.id = player.getUniqueId();
        this.name = player.getName();
        this.blackListedItems = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public boolean containsBlackListedItem(ItemStack itemStack) {
        return blackListedItems.stream().anyMatch(item -> item.isSimilar(itemStack));
    }

    public boolean containsBlackListedItem(Item item) {
        return blackListedItems.contains(item);
    }

    public List<Item> getBlackListedItems() {
        return blackListedItems;
    }

    public void addBlackListedItem(Item item) {
        if (!containsBlackListedItem(item)) {
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

    @Override
    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }
}
