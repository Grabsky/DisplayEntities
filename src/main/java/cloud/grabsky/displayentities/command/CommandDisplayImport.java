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
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.SuggestWith;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.node.ExecutionContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayImport {
    INSTANCE; // SINGLETON

    @Dependency
    private DisplayEntities plugin;

    @Dependency
    private PluginConfiguration configuration;

    private static final Pattern NAME_FORMAT = Pattern.compile("^[a-zA-Z0-9_/:.-]{1,48}$");

    @Command("display import <file> <name>")
    @CommandPermission("displayentities.command.display.import")
    public String onDisplayImport(
            final @NotNull Player sender,
            final @SuggestWith(FileSuggestionProvider.class) String file,
            final @NotNull String name
    ) {
        // Creating File instance pointing to the file specified by command sender.
        final File exportedFile = new File(new File(plugin.getDataFolder(), "exported"), file);
        // Sending error message if file does not have .ent extension or does not exist.
        if (exportedFile.getName().endsWith(".ent") == false || exportedFile.exists() == false)
            return configuration.messages().commandDisplayImportFailureFileNotFound().repl("{file}", MiniMessage.miniMessage().stripTags(file));
        // Sending error message if specified name does not match the format.
        if (NAME_FORMAT.matcher(name).matches() == false)
            return configuration.messages().commandDisplayImportFailureInvalidFormat();
        // Trying to read serialized entity from the file and then import it to a new display.
        try {
            final String serializedEntityBase64 = Files.readString(exportedFile.toPath(), StandardCharsets.UTF_8);
            // Decoding from Base64.
            final byte[] serializedEntity = Base64.getDecoder().decode(serializedEntityBase64.getBytes(StandardCharsets.UTF_8));
            // Creating the Entity instance. It is not yet spawned in the world.
            final Entity entity = Bukkit.getUnsafe().deserializeEntity(serializedEntity, sender.getWorld(), false, false);
            // Modifying display identifier stored inside PersistentDataContainer.
            DisplayWrapper.existing(entity).set(DisplayEntities.Keys.NAME, PersistentDataType.STRING, name);
            // Spawning imported entity at player's location.
            entity.spawnAt(entity.getLocation().set(sender.getX(), sender.getY(), sender.getZ()), CreatureSpawnEvent.SpawnReason.COMMAND);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayImportSuccess().repl("{name}", name).repl("{file}", MiniMessage.miniMessage().stripTags(file));
        } catch (final IOException e) {
            e.printStackTrace();
            return configuration.messages().commandDisplayImportFailureOther().repl("{file}", MiniMessage.miniMessage().stripTags(file));
        }
    }

    public static final class FileSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(final @NotNull ExecutionContext<BukkitCommandActor> context) {
            final File[] files = new File(DisplayEntities.instance().getDataFolder(), "exported").listFiles();
            // Returning list of files inside 'plugins/DisplayEntities/exported' directory.
            return (files != null && files.length > 0)
                    ? Stream.of(files).filter(it -> it.isFile() == true && it.getName().endsWith(".ent") == true).map(File::getName).toList()
                    : Collections.emptyList();
        }

    }

}
