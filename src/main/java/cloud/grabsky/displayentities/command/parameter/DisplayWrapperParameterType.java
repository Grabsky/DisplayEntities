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

import cloud.grabsky.displayentities.DisplayEntities;
import cloud.grabsky.displayentities.DisplayWrapper;
import com.google.common.reflect.TypeToken;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import revxrsal.commands.Lamp;
import revxrsal.commands.annotation.list.AnnotationList;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.InvalidValueException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum DisplayWrapperParameterType implements ParameterType.Factory<BukkitCommandActor> {
    INSTANCE; // SINGLETON

    @Override @SuppressWarnings("unchecked")
    public @Nullable ParameterType<BukkitCommandActor, ? extends DisplayWrapper> create(final @NotNull Type type, final @NotNull AnnotationList annotations, final @NotNull Lamp lamp) {
        // Skipping unsupported / unhandled types.
        if (TypeToken.of(type).isSubtypeOf(DisplayWrapper.class) == false)
            return null;
        // Getting raw Class<?> from the provided Type. This should never fail because of the check above.
        final Class<? extends DisplayWrapper> clazz = (Class<? extends DisplayWrapper>) TypeToken.of(type).getRawType();
        // Returning ParameterType instance.
        return new ParameterType<>() {

            @Override
            public DisplayWrapper parse(final @NotNull MutableStringStream input, final @NotNull ExecutionContext<BukkitCommandActor> context) {
                final String value = input.readString();
                // Player is REQUIRED for this parser to work.
                final Player sender = context.actor().requirePlayer();
                // Getting all applicable entities around the player.
                final @Nullable Display display = (Display) context.actor().requirePlayer().getNearbyEntities(64, 64, 64).stream()
                        .filter(it -> (it instanceof Display) && (it.getTrackedBy().contains(sender) == true) && it.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.NAME, PersistentDataType.STRING, "").equals(value) == true)
                        .findFirst().orElse(null);
                // Throwing exception if not found.
                if (display == null) {
                    throw new Exception(value);
                }
                // Creating DisplayWrapper from the found entity.
                final DisplayWrapper wrapper = DisplayWrapper.existing(display);
                // Throwing exception if the entity is not of the expected type.
                // This filters proceeding command suggestions and ensures command will not be executed when unsupported type is used.
                if (clazz.isInstance(wrapper) == false) {
                    throw new Exception(value);
                }
                // Otherwise, returning the wrapper instance.
                return wrapper;
            }

            @Override
            public @NotNull SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
                return context -> {
                    // Player is REQUIRED for any completions to work.
                    final Player sender = context.actor().requirePlayer();
                    // Filtering nearby entities and showing that in completions.
                    return context.actor().requirePlayer().getNearbyEntities(64, 64, 64).stream()
                            .filter(it -> (it instanceof Display) && (it.getTrackedBy().contains(sender) == true) && it.getPersistentDataContainer().has(DisplayEntities.Keys.NAME, PersistentDataType.STRING) == true)
                            .map(entity -> entity.getPersistentDataContainer().get(DisplayEntities.Keys.NAME, PersistentDataType.STRING))
                            .toList();
                };
            }

        };
    }

    /**
     * Represents an exception that is thrown when an invalid value is encountered
     * while parsing a {@link DisplayWrapper} argument.
     */
    public static final class Exception extends InvalidValueException {

        public Exception(final @NotNull String input) {
            super(input);
        }

    }

}
