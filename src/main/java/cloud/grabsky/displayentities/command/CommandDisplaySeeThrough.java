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
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplaySeeThrough {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> see_through")
    @CommandPermission("displayentities.command.display.edit.see_through")
    public String onDisplaySeeThrough(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Text display,
            final @Nullable @Optional Boolean seeThrough
    ) {
        final boolean finalState = (seeThrough != null) ? seeThrough : !display.entity().isSeeThrough();
        // Updating value of the see_through property of the display entity.
        display.entity().setSeeThrough(finalState);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditSeeThroughSuccess().repl("{state}", finalState);
    }

}
