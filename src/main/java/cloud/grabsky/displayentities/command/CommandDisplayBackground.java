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
package cloud.grabsky.displayentities.command;

import cloud.grabsky.displayentities.DisplayWrapper;
import cloud.grabsky.displayentities.configuration.PluginConfiguration;
import cloud.grabsky.displayentities.util.LombokExtensions;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayBackground {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> background")
    @CommandPermission("displayentities.command.display.edit.background")
    public String onDisplayBackground(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Text display,
            final @NotNull Color color,
            final @Nullable @Optional @Suggest({"0%", "25%", "50%", "75%"}) String opacity
    ) {
        // Calculating the opacity value if specified. It converts percentage value to an integer between 0 - 255.
        final Color finalColor = (opacity != null) ? color.setAlpha(Math.min(255, Math.round(Float.parseFloat(opacity.replace("%", "")) * 2.55F))) : color;
        // Updating value of the background_color property of the display entity.
        display.entity().setBackgroundColor(finalColor);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditBackgroundSuccess().repl("{color}", (finalColor.getAlpha() > 0) ? "#" + Integer.toHexString(finalColor.asARGB()).toUpperCase() : "transparent");
    }

}
