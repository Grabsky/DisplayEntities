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

