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
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum ClickCommandListener implements Listener {
    INSTANCE;

    private final Map<UUID, Long> lastClicked = new HashMap<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onDisplayAttack(final PrePlayerAttackEntityEvent event) {
        if (event.getAttacked() instanceof Mannequin || event.getAttacked() instanceof Interaction) {
            final boolean shouldCancel = handleEvent(event.getPlayer(), event.getAttacked());
            // Cancelling the event if desired.
            if (shouldCancel == true)
                event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onDisplayInteract(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Mannequin || event.getRightClicked() instanceof Interaction) {
            final boolean shouldCancel = handleEvent(event.getPlayer(), event.getRightClicked());
            // Cancelling the event if desired.
            if (shouldCancel == true)
                event.setCancelled(true);
        }
    }

    private boolean handleEvent(final Player player, final Entity entity) {
        if (entity.getPersistentDataContainer().has(DisplayEntities.Keys.NAME) == false)
            return false; // Not a DisplayEntities mannequin.
        // Returning if clicked entity does not have any command associated with it.
        if (entity.getPersistentDataContainer().has(DisplayEntities.Keys.CLICK_COMMAND) == false)
            return true;
        // Returning if player is on cooldown.
        if (System.currentTimeMillis() - this.lastClicked.getOrDefault(player.getUniqueId(), 0L) < 500L)
            return true;
        // Updating the last interaction time - putting player on a short cooldown.
        this.lastClicked.put(player.getUniqueId(), System.currentTimeMillis());
        // Getting the stored command.
        final String[] commands = PlaceholderAPI.setPlaceholders(player, entity.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.CLICK_COMMAND, PersistentDataType.STRING, "")).split("\\$AND");
        // Scheduling onto the GlobalRegionThread for Folia compatibility.
        Bukkit.getGlobalRegionScheduler().run(DisplayEntities.instance(), (task) -> {
            // Iterating over all specified commands.
            for (final String command : commands) {
                // Stripping leading slash if needed and trimming leading and trailing whitespaces.
                final String finalCommand = command.trim().startsWith("/") == true ? command.substring(1).trim() : command.trim();
                // Invoking command associated with the clicked entity, or doing nothing if it's blank.
                if (command.isBlank() == false) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
                }
            }
        });
        return true;
    }

}
