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
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPriority;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayBillboard {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @CommandPriority(0)
    @Command("display edit <display> billboard")
    @CommandPermission("displayentities.command.display.edit.billboard")
    public String onDisplayBillboard(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Strict display,
            final @NotNull Display.Billboard billboard
    ) {
        // Updating value of the billboard property of the display entity.
        display.entity(Display.class).setBillboard(billboard);
        // Returning (sending) success message to the sender.
        return configuration.messages().commandDisplayEditBillboardSuccess().repl("{billboard}", billboard.toString().toLowerCase());
    }

}
