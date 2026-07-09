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
package cloud.grabsky.displayentities.command.parameter;

import net.kyori.adventure.text.format.NamedTextColor;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.InvalidValueException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.Accessors;

public enum NamedTextColorParameterType implements ParameterType<BukkitCommandActor, NamedTextColor> {
    INSTANCE; // SINGLETON

    // Pre-calculating suggestions.
    private final List<String> suggestions = NamedTextColor.NAMES.keys().stream().toList();

    @Override
    public NamedTextColor parse(final @NotNull MutableStringStream input, final @NotNull ExecutionContext<BukkitCommandActor> context) {
        final String value = input.readString();
        // Getting NamedTextColor instance from the provided value.
        final @Nullable NamedTextColor color = NamedTextColor.NAMES.value(value);
        // Throwing exception if specified color does not exist.
        if (color == null)
            throw new Exception(value);
        // Otherwise, returning the color instance.
        return color;
    }

    @Override
    public @NotNull SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return SuggestionProvider.of(suggestions);
    }

    /**
     * Represents an exception that is thrown when an invalid color value is provided
     * while parsing a {@link NamedTextColorParameterType} argument.
     */
    @Accessors(fluent = true)
    public static final class Exception extends InvalidValueException {

        public Exception(final @NotNull String input) {
            super(input);
        }

    }

}

