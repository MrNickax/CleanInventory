package com.nickax.cleaninventory.inventory;

import com.nickax.cleaninventory.data.PlayerData;
import com.nickax.cleaninventory.item.Item;
import com.nickax.cleaninventory.repository.PlayerDataRepository;
import com.nickax.genten.item.ItemStackBuilder;
import com.nickax.genten.repository.dual.TargetRepository;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlackListInventoryProvider implements InventoryProvider {

    private final PlayerDataRepository playerDataRepository;

    public BlackListInventoryProvider(PlayerDataRepository playerDataRepository) {
        this.playerDataRepository = playerDataRepository;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        createMenu(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
       createMenu(player, contents);
    }

    private void createMenu(Player player, InventoryContents contents) {
        ClickableItem decoration = ClickableItem.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        contents.fill(decoration);

        Pagination pagination = contents.pagination();
        PlayerData playerData = playerDataRepository.get(player.getUniqueId(), TargetRepository.ONE);
        preparePagination(pagination, contents, playerData);

        contents.set(4, 1, ClickableItem.empty(new ItemStackBuilder(Material.OAK_SIGN)
                .setName("&aInformation")
                .setLore(
                        "&7You can manage the items blacklisted",
                        "&7from being picked up here.",
                        "&7",
                        "&eClick on an inventory item to add it!",
                        "&eClick on a blacklisted item to remove it!")
                .build()));

        if (!pagination.isFirst()) {
            contents.set(4, 3, ClickableItem.of(new ItemStackBuilder(Material.ARROW).setName("&aPrevious Page").build(),
                    event -> pagination.previous()));
        }
        contents.set(4, 4, ClickableItem.of(new ItemStackBuilder(Material.BARRIER).setName("&cClose").build(),
                event -> player.closeInventory()));
        if (!pagination.isLast()) {
            contents.set(4, 5, ClickableItem.of(new ItemStackBuilder(Material.ARROW).setName("&aNext Page").build(),
                    event -> pagination.next()));
        }
    }

    private void preparePagination(Pagination pagination, InventoryContents contents, PlayerData playerData) {
        List<Item> blackListedItems = playerData.getBlackListedItems();
        int size = blackListedItems.size();
        int closestMultipleOf18 = size == 0 ? 18 : ((size + 17) / 18) * 18;
        ClickableItem[] clickableItems = new ClickableItem[closestMultipleOf18];
        for (int i = 0; i < clickableItems.length; i++) {
            if (blackListedItems.size() > i) {
                Item blackListedItem = blackListedItems.get(i);
                ItemStack itemStack = new ItemStackBuilder(blackListedItem.toItemStack()).addToLore("&7", "&cClick to remove!").build();
                ClickableItem clickableItem = ClickableItem.of(itemStack, event -> playerData.removeBlackListedItem(blackListedItem));
                clickableItems[i] = clickableItem;
            } else {
                ItemStack itemStack = new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).setName("&cEmpty Slot")
                        .setLore("&7Click on an item to add it.").build();
                clickableItems[i] = ClickableItem.empty(itemStack);
            }
        }

        pagination.setItems(clickableItems);
        pagination.setItemsPerPage(18);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));
    }
}
