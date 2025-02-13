package com.nickax.cleaninventory.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class ItemProperties {

    public static final ItemProperties DEFAULT = new ItemProperties(null, null);

    private final String name;
    private final List<String> lore;

    public ItemProperties(ItemMeta parent) {
        this(parent.getDisplayName(), parent.getLore());
    }

    public ItemProperties(String name, List<String> lore) {
        this.name = name;
        this.lore = lore;
    }

    public ItemMeta asItemMeta(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null) {
            itemMeta.setDisplayName(name);
        }

        if (lore != null) {
            itemMeta.setLore(lore);
        }

        return itemMeta;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ItemProperties that = (ItemProperties) object;
        return Objects.equals(name, that.name) && Objects.equals(lore, that.lore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lore);
    }
}
