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

import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayCreate {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    private static final Pattern NAME_FORMAT = Pattern.compile("^[a-zA-Z0-9_/:.-]{1,48}$");

    @Command("display create")
    @CommandPermission("displayentities.command.display.create")
    public @NotNull String onDisplayCreate(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Type type,
            final @NotNull String name
    ) {
        // Sending error message if specified name does not match the format.
        if (NAME_FORMAT.matcher(name).matches() == false)
            return configuration.messages().commandDisplayCreateFailureInvalidFormat();
        // Getting player's location and stripping pitch and yaw from it.
        var location = sender.getLocation();
        // Stripping yaw and pitch if display is text, block or item.
        if (type == DisplayWrapper.Type.TEXT || type == DisplayWrapper.Type.BLOCK || type == DisplayWrapper.Type.ITEM)
            location = location.withPitch(0).withYaw(0);
        // Creating, spawning and configuring new display entity.
        final DisplayWrapper display = type.create(location, name).initialConfiguration();
        // Sending success message to the sender.
        return configuration.messages().commandDisplayCreateSuccess().repl("{name}", display.name());
    }

}
