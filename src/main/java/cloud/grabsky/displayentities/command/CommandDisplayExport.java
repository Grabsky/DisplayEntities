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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayExport {
    INSTANCE; // SINGLETON

    @Dependency
    private DisplayEntities plugin;

    @Dependency
    private PluginConfiguration configuration;

    @Command("display export <display>")
    @CommandPermission("displayentities.command.display.export")
    public String onDisplayExport(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display
    ) {
        // Serializing display to bytes and hashing with Base64.
        final byte[] serializedEntity = Bukkit.getUnsafe().serializeEntity(display.entity());
        // Serializing with Base64.
        final String serializedEntityBase64 = new String(Base64.getEncoder().encode(serializedEntity), StandardCharsets.UTF_8);
        // Creating File instance for the exported display.
        final File file = new File(new File(plugin.getDataFolder(), "exported"), display.name() + ".ent");
        // Creating directories if they don't exist.
        file.getParentFile().mkdirs();
        // Trying to write serialized entity to the file.
        try {
            Files.writeString(file.toPath(), serializedEntityBase64, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (final IOException e) {
            e.printStackTrace();
            return configuration.messages().commandDisplayExportFailure().repl("{display}");
        }
        // Sending success message to the sender.
        return configuration.messages().commandDisplayExportSuccess().repl("{display}", display.name()).repl("{file}", file.getName());
    }

}
