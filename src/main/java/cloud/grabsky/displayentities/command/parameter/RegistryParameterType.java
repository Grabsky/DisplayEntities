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

import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.InvalidValueException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

public final class RegistryParameterType<T extends Keyed> implements ParameterType<BukkitCommandActor, T> {

    private final @NotNull Class<T> registryType;
    private final @NotNull Supplier<Registry<@NotNull T>> registryAccess;

    // Filled in the constructor.
    private final List<String> suggestions;

    public RegistryParameterType(final @NotNull Class<T> registryType, final Supplier<Registry<T>> registryAccess) {
        this.registryType = registryType;
        this.registryAccess = registryAccess;
        // Pre-computing list of suggestions.
        this.suggestions = registryAccess.get().stream().map(Keyed::getKey).map(Key::asString).toList();
    }

    @Override
    public T parse(final @NotNull MutableStringStream input, final @NotNull ExecutionContext<BukkitCommandActor> context) {
        final String value = input.readString();
        // Creating NamespacedKey instance from the provided value.
        final @Nullable NamespacedKey key = NamespacedKey.fromString(value);
        // Throwing exception if NamespacedKey ended up being null.
        if (key == null)
            throw new Exception(value, registryType);
        // Getting the value from the registry.
        final @Nullable T obj = registryAccess.get().get(key);
        // Throwing exception if the object ended up being null.
        if (obj == null)
            throw new Exception(value, registryType);
        // Otherwise, returning the ItemType instance.
        return obj;
    }

    @Override
    public @NotNull SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return SuggestionProvider.of(suggestions);
    }

    /**
     * Represents an exception that is thrown when an invalid registry value is provided
     * while parsing a {@link RegistryParameterType} argument.
     */
    @Accessors(fluent = true)
    public static final class Exception extends InvalidValueException {

        /**
         * Represents the type of registry associated with the specific key.
         */
        @Getter(AccessLevel.PUBLIC)
        private final Class<? extends Keyed> registryType;

        public Exception(final @NotNull String input, final @NotNull Class<? extends Keyed> registryType) {
            super(input);
            this.registryType = registryType;
        }

    }

}

