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

import cloud.grabsky.displayentities.DisplayEntities;
import cloud.grabsky.displayentities.DisplayWrapper;
import com.google.common.reflect.TypeToken;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Mannequin;
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
                // Getting the configured radius for nearby entities lookup.
                final int radius = DisplayEntities.instance().configuration().nearbyEntitiesRadius();
                // Getting all applicable entities around the player.
                final @Nullable Entity display = context.actor().requirePlayer().getNearbyEntities(radius, radius, radius).stream()
                        .filter(it -> (it instanceof Display || it instanceof Interaction || it instanceof Mannequin) && (it.getTrackedBy().contains(sender) == true) && it.getPersistentDataContainer().getOrDefault(DisplayEntities.Keys.NAME, PersistentDataType.STRING, "").equals(value) == true)
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
                    // Getting the configured radius for nearby entities lookup.
                    final int radius = DisplayEntities.instance().configuration().nearbyEntitiesRadius();
                    // Filtering nearby entities and showing that in completions.
                    return context.actor().requirePlayer().getNearbyEntities(radius, radius, radius).stream()
                            .filter(it -> (it instanceof Display || it instanceof Interaction || it instanceof Mannequin) && (it.getTrackedBy().contains(sender) == true) && it.getPersistentDataContainer().has(DisplayEntities.Keys.NAME, PersistentDataType.STRING) == true)
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
