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
public enum CommandDisplayRotateX {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> rotate_x")
    @CommandPermission("displayentities.command.display.edit.rotate_x")
    public String onDisplayRotateX(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Strict display,
            final @NotNull Float degrees
    ) {
        // Teleporting entity to desired location.
        display.entity().setRotation(display.entity().getYaw(), Math.clamp(display.entity().getPitch() + degrees, -90F, 90F));
        // Returning (sending) success message to the sender.
        return configuration.messages().commandDisplayEditRotateXSuccess().repl("{pitch}", String.format("%.2f", display.entity().getPitch()));
    }

    @Command("display edit <display> rotate_x")
    @CommandPermission("displayentities.command.display.edit.rotate_x")
    public String onDisplayRotateX(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull Float degrees
    ) {
        // Teleporting entity to desired location.
        display.entity().setRotation(display.entity().getYaw(), Math.clamp(display.entity().getPitch() + degrees, -90F, 90F));
        // Returning (sending) success message to the sender.
        return configuration.messages().commandDisplayEditRotateXSuccess().repl("{pitch}", String.format("%.2f", display.entity().getPitch()));
    }

}
