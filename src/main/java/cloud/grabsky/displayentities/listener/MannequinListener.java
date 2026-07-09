/*
 * DisplayEntities (https://github.com/Grabsky/DisplayEntities)
 *
 * Copyright (C) 2026  Grabsky <michal.czopek.foss@proton.me>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License v3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License v3 for more details.
 */
package cloud.grabsky.displayentities.listener;

import cloud.grabsky.displayentities.DisplayEntities;
import org.bukkit.entity.Mannequin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public enum MannequinListener implements Listener {
    INSTANCE;

    // Cancelling mannequins income damage if they are (1) created by this plugin and (2) are marked as invulnerable.
    @EventHandler(ignoreCancelled = true)
    public void onMannequinDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Mannequin mannequin)
            if (mannequin.getPersistentDataContainer().has(DisplayEntities.Keys.NAME) == true && mannequin.isInvulnerable() == true)
                event.setCancelled(true);
    }

    // Cancelling mannequins entering vehicles if they are (1) created by this plugin and (2) are marked as immovable.
    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(final VehicleEnterEvent event) {
        if (event.getEntered() instanceof Mannequin mannequin)
            if (mannequin.getPersistentDataContainer().has(DisplayEntities.Keys.NAME) == true && mannequin.isImmovable() == true)
                event.setCancelled(true);
    }

}
