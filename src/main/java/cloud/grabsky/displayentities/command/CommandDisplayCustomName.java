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
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.SuggestWith;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.node.ExecutionContext;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayCustomName {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> custom_name")
    @CommandPermission("displayentities.command.display.edit.custom_name")
    public String onDisplayCustomName(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @SuggestWith(CustomNameSuggestionProvider.class) String name
    ) {
        // Clearing name if set to @hidden selector.
        if (name.equalsIgnoreCase("@hidden") == true) {
            display.entity().customName(null);
            display.entity().setCustomNameVisible(false);
            // Removing custom name from mannequin's PersistentDataContainer.
            display.remove(DisplayEntities.Keys.MANNEQUIN_CUSTOM_NAME);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditCustomNameSuccess();
        } else {
            display.entity().setRichCustomName(name);
            display.entity().setCustomNameVisible(true);
            // Updating custom name inside mannequin's PersistentDataContainer.
            display.set(DisplayEntities.Keys.MANNEQUIN_CUSTOM_NAME, PersistentDataType.STRING, name);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditCustomNameSuccess();
        }
    }

    public static final class CustomNameSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(final @NotNull ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper.Mannequin argument.
            final @Nullable DisplayWrapper.Mannequin wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.Mannequin.class);
            // If wrapper is not null, returning suggestions based on entity properties.
            if (wrapper != null) {
                final @Nullable String name = wrapper.get(DisplayEntities.Keys.MANNEQUIN_CUSTOM_NAME, PersistentDataType.STRING);
                return (name != null)
                        ? List.of("@hidden", name)
                        : Collections.singletonList("@hidden");
            }
            // Otherwise, returning an empty list.
            return Collections.emptyList();
        }

    }

}
