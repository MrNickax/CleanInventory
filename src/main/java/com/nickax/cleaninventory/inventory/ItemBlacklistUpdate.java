package com.nickax.cleaninventory.inventory;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.genten.inventory.BaseInventory;
import com.nickax.genten.inventory.action.Action;
import com.nickax.genten.inventory.action.ActionTrigger;
import com.nickax.genten.inventory.action.task.Task;
import com.nickax.genten.inventory.item.InventoryItem;
import com.nickax.genten.item.Item;
import com.nickax.genten.item.ItemProperties;
import com.nickax.genten.language.operation.LanguageAccessor;
import com.nickax.genten.repository.Repository;
import com.nickax.genten.text.BaseText;
import com.nickax.genten.text.LocalizedText;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ItemBlacklistUpdate implements BiConsumer<Player, BaseInventory> {

    private final Repository<UUID, PlayerData> cache;
    private final LanguageAccessor languageAccessor;

    public ItemBlacklistUpdate(CleanInventory plugin) {
        this.cache = plugin.getPlayerDataCache();
        this.languageAccessor = plugin.getLanguageAccessor();
    }

    @Override
    public void accept(Player player, BaseInventory inventory) {
        PlayerData playerData = cache.get(player.getUniqueId());
        List<InventoryItem> items = createInventoryItems(playerData);
        inventory.getContent().getPagination().setItems(items);
    }

    private List<InventoryItem> createInventoryItems(PlayerData playerData) {
        List<InventoryItem> items = new ArrayList<>();
        playerData.getBlackListedItems().forEach(item ->
                addItemToList(playerData, item, items)
        );
        return items;
    }

    private void addItemToList(PlayerData playerData, Item item, List<InventoryItem> items) {
        Item icon = getIcon(item);
        Action action = createAction(playerData, item);
        InventoryItem inventoryItem = new InventoryItem(icon, true, List.of(action));
        items.add(inventoryItem);
    }

    private Item getIcon(Item base) {
        ItemProperties properties = ItemProperties.fromItemProperties(base.getProperties()).build();
        return new Item(base.getMaterial().name(), properties);
    }

    private Action createAction(PlayerData playerData, Item item) {
        Task task = player -> playerData.removeBlackListedItem(item);
        return new Action(ActionTrigger.CLICK, List.of(task));
    }
}
