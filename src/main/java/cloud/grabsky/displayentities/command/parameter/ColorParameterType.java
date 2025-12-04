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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ColorParameterType implements ParameterType<BukkitCommandActor, Color> {
    INSTANCE; // SINGLETON

    // Holds map of common colors identified by their name.
    public static final Map<String, Color> NAMED_COLOR_MAP = Map.ofEntries(
            Map.entry("aqua", Color.AQUA),
            Map.entry("black", Color.BLACK),
            Map.entry("blue", Color.BLUE),
            Map.entry("fuchsia", Color.FUCHSIA),
            Map.entry("gray", Color.GRAY),
            Map.entry("green", Color.GREEN),
            Map.entry("lime", Color.LIME),
            Map.entry("maroon", Color.MAROON),
            Map.entry("navy", Color.NAVY),
            Map.entry("olive", Color.OLIVE),
            Map.entry("orange", Color.ORANGE),
            Map.entry("purple", Color.PURPLE),
            Map.entry("red", Color.RED),
            Map.entry("silver", Color.SILVER),
            Map.entry("teal", Color.TEAL),
            Map.entry("white", Color.WHITE),
            Map.entry("yellow", Color.YELLOW)
    );

    // Holds a list of suggestions for named colors.
    private static final List<String> NAMED_COLOR_SUGGESTIONS = NAMED_COLOR_MAP.keySet().stream().toList();

    @Override
    public Color parse(final @NotNull MutableStringStream input, final @NotNull ExecutionContext<BukkitCommandActor> context) {
        final String value = input.readString().toLowerCase();
        // Trying to get the value from named map first.
        final @Nullable Color color = Optional.ofNullable(NAMED_COLOR_MAP.get(value)).orElseGet(() -> {
            // Removing the leading '#' from the value.
            String hex = (value.startsWith("#") == true) ? value.substring(1) : value;
            // Expanding short colors. (#RGB -> #RRGGBB, #ARGB -> #AARRGGBB)
            if (hex.length() == 3 || hex.length() == 4) {
                // Preparing the result StringBuilder instance.
                final StringBuilder result = new StringBuilder();
                // Duplicating each character in the initial value.
                for (final char c : hex.toCharArray())
                    result.append(c).append(c);
                // Overriding previous value with new one.
                hex = result.toString();
            }
            // Returning 'null' if value is of unsupported format.
            if (hex.length() != 6 && hex.length() != 8)
                return null;
            try {
                // Converting the specified String (hex) color to an integer.
                int intColor = (int) Long.parseLong(hex, 16);
                // Adding alpha if unspecified.
                if (hex.length() == 6)
                    intColor |= 0xFF000000;
                // Parsing the color.
                return Color.fromARGB(
                        (intColor >> 24) & 0xFF,
                        (intColor >> 16) & 0xFF,
                        (intColor >> 8) & 0xFF,
                        (intColor & 0xFF)
                );
            } catch (final IllegalArgumentException e) {
                return null;
            }
        });
        // Throwing exception if colors ended up being null.
        if (color == null)
            throw new Exception(value);
        // Returning the color value.
        return color;
    }

    @Override
    public @NotNull SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return SuggestionProvider.of(NAMED_COLOR_SUGGESTIONS);
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
