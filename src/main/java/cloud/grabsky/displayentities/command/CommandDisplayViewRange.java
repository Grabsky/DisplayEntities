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
public enum CommandDisplayViewRange {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> view_range")
    @CommandPermission("displayentities.command.display.edit.view_range")
    public String onDisplayViewRange(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Strict display,
            final @NotNull @SuggestWith(ViewDistanceSuggestionProvider.class) Float viewRange
    ) {
        // Calculating the view range value. Cannot be less than 0.
        final float finalViewRange = Math.max(0F, viewRange);
        // Updating value of the view_range property of the display entity.
        display.entity(Display.class).setViewRange(finalViewRange);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditViewRangeSuccess().repl("{range}", finalViewRange);
    }

    /* SUGGESTION PROVIDERS */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ViewDistanceSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Generating and returning suggestions.
            return (wrapper != null) ? Collections.singletonList(String.format("%.2f", wrapper.entity(Display.class).getViewRange())) : Collections.emptyList();
        }

    }

}
