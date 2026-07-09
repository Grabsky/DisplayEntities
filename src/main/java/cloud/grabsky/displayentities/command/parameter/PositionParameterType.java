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

import io.papermc.paper.math.Position;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.InvalidValueException;
import revxrsal.commands.exception.MissingArgumentException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage") // Position
public enum PositionParameterType implements ParameterType<BukkitCommandActor, Position> {
    INSTANCE; // SINGLETON

    @Override
    public Position parse(final @NotNull MutableStringStream input, final @NotNull ExecutionContext<BukkitCommandActor> context) {
        final String rawX = input.readString();
        consumeSpace(input);
        final String rawY = input.readString();
        consumeSpace(input);
        final String rawZ = input.readString();
        // Constructing and returning Position argument, or throwing exception otherwise.
        try {
            final double x = (rawX.equals("~") == true) ? context.actor().requirePlayer().getX() : Double.parseDouble(rawX);
            final double y = (rawY.equals("~") == true) ? context.actor().requirePlayer().getY() : Double.parseDouble(rawY);
            final double z = (rawZ.equals("~") == true) ? context.actor().requirePlayer().getZ() : Double.parseDouble(rawZ);
            // Returning the value.
            return Position.fine(x, y, z);
        } catch (final NumberFormatException e) {
            throw new NumberException(rawX + " " + rawY + " " + rawZ);
        }
    }

    @Override
    public @NotNull SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return SuggestionProvider.of("~ ~ ~");
    }

    private static void consumeSpace(final @NotNull MutableStringStream input) {
        if (input.hasRemaining() == true && input.peek() == ' ')
            input.moveForward();
    }

    /**
     * Represents an exception that is thrown when an invalid coordinate value is provided
     * while parsing a {@link Position} argument.
     */
    public static final class NumberException extends InvalidValueException {

        public NumberException(final @NotNull String input) {
            super(input);
        }

    }

}
