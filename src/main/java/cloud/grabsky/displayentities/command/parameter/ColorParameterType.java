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

import org.bukkit.Color;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.InvalidValueException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ColorParameterType implements ParameterType<BukkitCommandActor, Color> {
    INSTANCE; // SINGLETON

    @Override
    public Color parse(final @NotNull MutableStringStream input, final @NotNull ExecutionContext<BukkitCommandActor> context) {
        final String value = input.readString().toLowerCase();
        final @Nullable Color color = switch (value) {
            case "aqua" -> Color.AQUA;
            case "black" -> Color.BLACK;
            case "blue" -> Color.BLUE;
            case "fuchsia" -> Color.FUCHSIA;
            case "gray" -> Color.GRAY;
            case "green" -> Color.GREEN;
            case "lime" -> Color.LIME;
            case "maroon" -> Color.MAROON;
            case "navy" -> Color.NAVY;
            case "olive" -> Color.OLIVE;
            case "orange" -> Color.ORANGE;
            case "purple" -> Color.PURPLE;
            case "red" -> Color.RED;
            case "silver" -> Color.SILVER;
            case "teal" -> Color.TEAL;
            case "white" -> Color.WHITE;
            case "yellow" -> Color.YELLOW;
            // Otherwise, we'll try to parse it as ARGB hex.
            default -> {
                final String hex = value.replace("#", "");
                try {
                    // Converting the specified String (hex) color to an integer.
                    int intColor = (int) Long.parseLong(hex, 16);
                    // Adding alpha if unspecified.
                    if (hex.length() == 6)
                        intColor |= 0xFF000000;
                    // Parsing the color.
                    yield Color.fromARGB(
                            (intColor >> 24) & 0xFF,
                            (intColor >> 16) & 0xFF,
                            (intColor >> 8) & 0xFF,
                            (intColor & 0xFF)
                    );
                } catch (final IllegalArgumentException e) {
                    yield null;
                }
            }
        };
        // Throwing exception if colors ended up being null.
        if (color == null)
            throw new Exception(value);
        // Returning the color value.
        return color;
    }

    @Override
    public @NotNull SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return SuggestionProvider.of("aqua", "black", "blue", "fuchsia", "gray", "green", "lime", "maroon", "navy", "olive", "orange", "purple", "red", "silver", "teal", "white", "yellow");
    }

    /**
     * Represents an exception that is thrown when an invalid color value is provided
     * while parsing a {@link Color} argument.
     */
    public static final class Exception extends InvalidValueException {

        public Exception(final @NotNull String input) {
            super(input);
        }

    }

}
