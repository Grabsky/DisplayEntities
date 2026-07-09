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
package cloud.grabsky.displayentities.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.TextDisplay;

import org.jetbrains.annotations.NotNull;

public enum LombokExtensions {
    INSTANCE; // SINGLETON

    public static String repl(final @NotNull String self, @NotNull Object... replacements) {
        // Returning the original string if no replacements were specified.
        if (replacements == null)
            return self;
        // Throwing exception when
        if (replacements.length % 2 != 0)
            throw new IllegalArgumentException("Invalid arguments. Replacements must be in key-value pairs.");
        // Preparing the result string.
        String result = self;
        // Iterating over specified replacements key-value pairs and replacing them in the string.
        for (int index = 0; index < replacements.length; index += 2)
            result = result.replace(String.valueOf(replacements[index]), String.valueOf(replacements[index + 1]));
        // Returning the result.
        return result;
    }

    public static void setRichText(final TextDisplay display, final String text) {
        display.text(MiniMessage.miniMessage().deserialize(text));
    }

    public static void setRichDescription(final Mannequin mannequin, final String text) {
        mannequin.setDescription(MiniMessage.miniMessage().deserialize(text));
    }

    public static void setRichCustomName(final Nameable nameable, final String text) {
        nameable.customName(MiniMessage.miniMessage().deserialize(text));
    }

    public static Location withPitch(final Location location, final float pitch) {
        final Location copied = location.clone();
        copied.setPitch(pitch);
        return copied;
    }

    public static Location withYaw(final Location location, final float yaw) {
        final Location copied = location.clone();
        copied.setYaw(yaw);
        return copied;
    }

}
