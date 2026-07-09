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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public enum CommandDisplayClickCommand {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> click_command <command>")
    @CommandPermission("displayentities.command.display.edit.click_command")
    public String onDisplayClickCommand(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Interaction display,
            final @NotNull @SuggestWith(ClickCommandSuggestionProvider.class) String command
    ) {
        // When command is specified as '@none', click command is removed from data container.
        if (command.equalsIgnoreCase("@none") == true) {
            display.remove(DisplayEntities.Keys.CLICK_COMMAND);
            return configuration.messages().commandDisplayEditClickCommandSuccess();
        }
        // Otherwise, setting / updating click command.
        display.set(DisplayEntities.Keys.CLICK_COMMAND, PersistentDataType.STRING, command);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditClickCommandSuccess();
    }

    @Command("display edit <display> click_command <command>")
    @CommandPermission("displayentities.command.display.edit.click_command")
    public String onDisplayClickCommand(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @SuggestWith(ClickCommandSuggestionProvider.class) String command
    ) {
        // When command is specified as '@none', click command is removed from data container.
        if (command.equalsIgnoreCase("@none") == true) {
            display.remove(DisplayEntities.Keys.CLICK_COMMAND);
            return configuration.messages().commandDisplayEditClickCommandSuccess();
        }
        // Otherwise, setting / updating click command.
        display.set(DisplayEntities.Keys.CLICK_COMMAND, PersistentDataType.STRING, command);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditClickCommandSuccess();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ClickCommandSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(final @NotNull ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Generating and returning suggestions.
            return (wrapper != null)
                    ? List.of("@none", wrapper.get(DisplayEntities.Keys.CLICK_COMMAND, PersistentDataType.STRING, ""))
                    : Collections.singletonList("@none");
        }

    }

}
