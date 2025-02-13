package com.nickax.cleaninventory.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Item {

    private final String type;
    private final ItemProperties properties;

    public Item(ItemStack parent) {
        this(parent.getType().name(), new ItemProperties(parent.getItemMeta()));
    }

    public Item(String type) {
        this(type, ItemProperties.DEFAULT);
    }

    public Item(String type, ItemProperties properties) {
        this.type = type;
        this.properties = properties;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(getMaterial());
        ItemMeta itemMeta = properties.asItemMeta(itemStack);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Material getMaterial() {
        return Material.valueOf(type);
    }

    public String getType() {
        return type;
    }

    public ItemProperties getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Item item = (Item) object;
        return Objects.equals(type, item.type) && Objects.equals(properties, item.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, properties);
    }
}
