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
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayClone {
    INSTANCE; // SINGLETON

    @Dependency
    private DisplayEntities plugin;

    @Dependency
    private PluginConfiguration configuration;

    private static final Pattern NAME_FORMAT = Pattern.compile("^[a-zA-Z0-9_/:.-]{1,48}$");

    @Command("display clone")
    @CommandPermission("displayentities.command.display.clone")
    public String onDisplayClone(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display,
            final @NotNull String name
    ) {
        // Sending error message if specified name does not does not match the format.
        if (NAME_FORMAT.matcher(name).matches() == false)
            return configuration.messages().commandDisplayCloneFailureInvalidFormat();
        // Cloning the display entity.
        final Display clone = (Display) display.entity().copy();
        // Creating location for entity to be spawned at. This is sender's location but with yaw and pitch kept from the original entity.
        final Location location = sender.getLocation().withYaw(clone.getYaw()).withPitch(clone.getPitch());
        // Spawning a clone of display entity.
        sender.getScheduler().run(plugin, (it) -> clone.spawnAt(location), null);
        // Creating a DisplayWrapper instance for the copied entity. This method should override the ID stored in PDC.
        DisplayWrapper.create(clone, name);
        // Returning (sending) message to the sender.
        return configuration.messages().commandDisplayCloneSuccess().repl("{original_name}", display.name(), "{copied_name}", name);
    }

}
