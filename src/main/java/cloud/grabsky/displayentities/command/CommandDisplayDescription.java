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
public enum CommandDisplayDescription {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> description")
    @CommandPermission("displayentities.command.display.edit.description")
    public String onDisplayDescription(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @SuggestWith(DescriptionSuggestionProvider.class) String description
    ) {
        // Clearing description if set to @hidden selector.
        if (description.equalsIgnoreCase("@hidden") == true) {
            display.entity().setDescription(null);
            // Removing description from mannequin's PersistentDataContainer.
            display.remove(DisplayEntities.Keys.MANNEQUIN_DESCRIPTION);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditDescriptionSuccess();
        } else {
            display.entity().setRichDescription(description);
            // Updating description inside mannequin's PersistentDataContainer.
            display.set(DisplayEntities.Keys.MANNEQUIN_DESCRIPTION, PersistentDataType.STRING, description);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditDescriptionSuccess();
        }
    }

    public static final class DescriptionSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(final @NotNull ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper.Mannequin argument.
            final @Nullable DisplayWrapper.Mannequin wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.Mannequin.class);
            // If wrapper is not null, returning suggestions based on entity properties.
            if (wrapper != null) {
                final @Nullable String description = wrapper.get(DisplayEntities.Keys.MANNEQUIN_DESCRIPTION, PersistentDataType.STRING);
                return (description != null)
                        ? List.of("@hidden", description)
                        : Collections.singletonList("@hidden");
            }
            // Otherwise, returning an empty list.
            return Collections.emptyList();
        }

    }

}
