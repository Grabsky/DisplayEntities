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
public enum CommandDisplayRefreshInterval {
    INSTANCE; // SINGLETON

    @Dependency
    private DisplayEntities plugin;

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> refresh_interval")
    @CommandPermission("displayentities.command.display.edit.refresh_interval")
    public String onDisplayAlignment(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Text display,
            final @NotNull @SuggestWith(IntervalSuggestionProvider.class) String interval
    ) {
        if (interval.equals("default") == true) {
            // Removing the stored refresh_interval so it takes the config value instead.
            display.remove(DisplayEntities.Keys.REFRESH_INTERVAL);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditRefreshIntervalSuccess().repl("{ticks}", configuration.refreshInterval());
        }
        final @Nullable Integer parsedInterval = parseInt(interval);
        // Sending error message if invalid value was found.
        if (parsedInterval == null)
            return configuration.messages().commandDisplayEditRefreshIntervalFailure();
        // Setting refresh_interval of this display entity.
        display.set(DisplayEntities.Keys.REFRESH_INTERVAL, PersistentDataType.INTEGER, Math.max(1, parsedInterval));
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditRefreshIntervalSuccess().repl("{ticks}", Math.max(1, parsedInterval));
    }

    /* HELPER METHODS */

    private static @Nullable Integer parseInt(final @NotNull String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /* SUGGESTION PROVIDER */

    public static final class IntervalSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(final @NotNull ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Returning empty list if wrapper was unspecified.
            return (wrapper != null)
                    ? (wrapper.get(DisplayEntities.Keys.REFRESH_INTERVAL, PersistentDataType.INTEGER) != null)
                            ? List.of("" + wrapper.get(DisplayEntities.Keys.REFRESH_INTERVAL, PersistentDataType.INTEGER), "default")
                            : Collections.singletonList("default")
                    : Collections.emptyList();
        }

    }

}
