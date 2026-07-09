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
import io.papermc.paper.math.Position;
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
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("UnstableApiUsage") // Position
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayMoveTo {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> move_to")
    @CommandPermission("displayentities.command.display.edit.move_to")
    public String onDisplayMoveTo(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display,
            final @NotNull @SuggestWith(CurrentPositionSuggestionProvider.class) Position position
    ) {
        // Teleporting entity to desired location.
        display.entity().teleportAsync(position.toLocation(display.entity().getWorld()));
        // Returning (sending) success message to the sender.
        return configuration.messages().commandDisplayEditMoveToSuccess()
                .repl("{x}", String.format("%.2f", position.x()))
                .repl("{y}", String.format("%.2f", position.y()))
                .repl("{z}", String.format("%.2f", position.z()));
    }

    /* SUGGESTION PROVIDERS */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class CurrentPositionSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Returning empty completions if wrapper is null.
            return (wrapper != null)
                    ? List.of("~ ~ ~", String.format("%.2f %.2f %.2f", wrapper.entity().getX(), wrapper.entity().getY(), wrapper.entity().getZ()))
                    : Collections.emptyList();
        }

    }

}
