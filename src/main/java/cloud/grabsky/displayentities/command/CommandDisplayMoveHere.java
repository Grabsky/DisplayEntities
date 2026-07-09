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
public enum CommandDisplayMoveHere {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> move_here")
    @CommandPermission("displayentities.command.display.edit.move_here")
    public String onDisplayMoveHere(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display
    ) {
        var position = sender.getLocation();
        // Teleporting entity to desired location.
        display.entity().teleportAsync(position);
        // Returning (sending) success message to the sender.
        return configuration.messages().commandDisplayEditMoveHereSuccess()
                .repl("{x}", String.format("%.2f", position.x()))
                .repl("{y}", String.format("%.2f", position.y()))
                .repl("{z}", String.format("%.2f", position.z()));
    }

}
