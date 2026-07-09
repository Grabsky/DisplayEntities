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

import cloud.grabsky.displayentities.DisplayWrapper;
import cloud.grabsky.displayentities.configuration.PluginConfiguration;
import cloud.grabsky.displayentities.util.LombokExtensions;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayTeleport {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display teleport <display>")
    @CommandPermission("displayentities.command.display.teleport")
    public String onDisplayTeleport(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display
    ) {
        // Teleporting entity to the location of display entity.
        sender.teleportAsync(display.entity().getLocation());
        // Returning (sending) success message to the sender.
        return configuration.messages().commandDisplayTeleportSuccess().repl("{display}", display.name());
    }

}
