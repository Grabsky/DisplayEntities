/*
 * DisplayEntities (https://github.com/Grabsky/DisplayEntities)
 *
 * MIT License
 *
 * Copyright (c) 2025 Grabsky (michal.czopek.foss@proton.me)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
