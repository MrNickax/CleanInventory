package com.nickax.cleaninventory.listener.pickup;

import com.nickax.cleaninventory.CleanInventory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class StandardPickupListener extends PickupListener {

    public StandardPickupListener(CleanInventory plugin) {
        super(plugin);
    }

    @EventHandler
    private void onEntityPickupItem(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            event.setCancelled(cancelPickup((Player) entity, event.getItem().getItemStack()));
        }
    }
}
