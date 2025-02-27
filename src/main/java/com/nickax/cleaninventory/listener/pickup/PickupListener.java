package com.nickax.cleaninventory.listener.pickup;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.genten.listener.SwitchableListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupListener extends SwitchableListener {

    private final PickupHandler pickupHandler;

    public PickupListener(CleanInventory plugin) {
        super(plugin);
        this.pickupHandler = new PickupHandler(plugin);
    }

    @EventHandler
    private void onEntityPickupItem(EntityPickupItemEvent event) {
        Player player = (Player) event.getEntity();
        ItemStack item = event.getItem().getItemStack();
        pickupHandler.handle(player, item, event);
    }
}
