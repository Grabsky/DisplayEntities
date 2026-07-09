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
package cloud.grabsky.displayentities.command;

import cloud.grabsky.displayentities.DisplayEntities;
import cloud.grabsky.displayentities.DisplayWrapper;
import cloud.grabsky.displayentities.configuration.PluginConfiguration;
import cloud.grabsky.displayentities.util.LombokExtensions;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayRespawn {
    INSTANCE; // SINGLETON

    @Dependency
    private DisplayEntities plugin;

    @Dependency
    private PluginConfiguration configuration;

    @Command("display respawn")
    @CommandPermission("displayentities.command.display.respawn")
    public String onDisplayRespawn(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display
    ) {
        // Getting location of the entity.
        final Location location = display.entity().getLocation();
        // Copying the display entity.
        final Entity copy = display.entity().copy();
        // Removing the original display entity.
        display.entity().remove();
        // Spawning a copy of display entity.
        sender.getScheduler().run(plugin, (it) -> {
            copy.spawnAt(location, CreatureSpawnEvent.SpawnReason.COMMAND);
            // Creating the DisplayWrapper instance from the copied entity.
            final DisplayWrapper newDisplay = DisplayWrapper.existing(copy);
            // ...for syncing things like sitting pose; currently unused.
        }, null);
        // Returning (sending) message to the sender.
        return configuration.messages().commandDisplayRespawnSuccess().repl("{name}", display.name());
    }

}
