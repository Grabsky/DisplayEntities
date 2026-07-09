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
import org.bukkit.entity.TextDisplay;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayAlignment {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> alignment")
    @CommandPermission("displayentities.command.display.edit.alignment")
    public String onDisplayAlignment(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Text display,
            final @NotNull TextDisplay.TextAlignment alignment
    ) {
        // Updating value of the alignment property of the display entity.
        display.entity().setAlignment(alignment);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditAlignmentSuccess().repl("{alignment}", alignment.toString().toLowerCase());
    }

}
