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
import revxrsal.commands.annotation.SuggestWith;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.node.ExecutionContext;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayHeight {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> height")
    @CommandPermission("displayentities.command.display.edit.height")
    public String onDisplayHeight(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Interaction display,
            final @NotNull @SuggestWith(HeightSuggestionProvider.class) Float height
    ) {
        // Calculating the final height value.
        final float finalHeight = Math.max(0, height);
        // Setting height of the interaction entity.
        display.entity().setInteractionHeight(finalHeight);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditHeightSuccess().repl("{height}", finalHeight);
    }

    /* SUGGESTION PROVIDERS */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class HeightSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper.Interaction wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.Interaction.class);
            // Generating and returning suggestions.
            return (wrapper != null) ? Collections.singletonList(wrapper.entity().getInteractionHeight() + "") : Collections.emptyList();
        }

    }

}
