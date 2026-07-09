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
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.ExtensionMethod;

// NOTE: Glowing effect can technically be applied to any display entity, but is only visible on block and item displays.
//       That's the reason why command logic is duplicated for each "working" display type.
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayGlow {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;


    /* ITEM DISPLAY */

    @Command("display edit <display> glow")
    @CommandPermission("displayentities.command.display.edit.glow")
    public String onItemDisplayGlow(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Item display,
            final @NotNull Color color
    ) {
        // Enabling glow state and updating value of the glow color override property of the display.
        display.entity().setGlowing(true);
        display.entity().setGlowColorOverride(color);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditGlowColorChangeSuccess().repl("{color}", "#" + Integer.toHexString(color.asRGB()).toUpperCase());
    }

    @Command("display edit <display> glow @none")
    @CommandPermission("displayentities.command.display.edit.glow")
    public String onItemDisplayGlow(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Item display
    ) {
        // Disabling glow state of the display.
        display.entity().setGlowing(false);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditGlowDisabledSuccess();
    }


    /* BLOCK DISPLAY */

    @Command("display edit <display> glow")
    @CommandPermission("displayentities.command.display.edit.glow")
    public String onBlockDisplayGlow(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Block display,
            final @NotNull Color color
    ) {
        // Enabling glow state and updating value of the glow color override property of the display.
        display.entity().setGlowing(true);
        display.entity().setGlowColorOverride(color);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditGlowColorChangeSuccess().repl("{color}", "#" + Integer.toHexString(color.asRGB()).toUpperCase());
    }

    @Command("display edit <display> glow @none")
    @CommandPermission("displayentities.command.display.edit.glow")
    public String onBlockDisplayGlow(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Block display
    ) {
        // Disabling glow state of the display.
        display.entity().setGlowing(false);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditGlowDisabledSuccess();
    }


    /* MANNEQUIN */

    @Command("display edit <display> glow")
    @CommandPermission("displayentities.command.display.edit.glow")
    public String onMannequinGlow(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull NamedTextColor color
    ) {
        final String colorKey = NamedTextColor.NAMES.keyOr(color, "null");
        // Enabling glow state and updating glow color of the mannequin.
        display.entity().setGlowing(true);
        // Getting scoreboard team for the specified color.
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("de_" + colorKey);
        // Creating the team if non-existent.
        if (team == null) {
            team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("de_" + colorKey);
            team.color(color);
        }
        // Adding entity to the team.
        team.addEntity(display.entity());
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditGlowColorChangeSuccess().repl("{color}", colorKey);
    }

    @Command("display edit <display> glow @none")
    @CommandPermission("displayentities.command.display.edit.glow")
    public String onMannequinGlow(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display
    ) {
        // Disabling glow state of the mannequin.
        display.entity().setGlowing(false);
        // Getting the team this entity is currently in.
        final @Nullable Team team = Bukkit.getScoreboardManager().getMainScoreboard().getEntityTeam(display.entity());
        // Removing entity from their team if specified.
        if (team != null) {
            team.removeEntity(display.entity());
            // Unregistering (deleting) the team if it's empty.
            if (team.getSize() == 0)
                team.unregister();
        }
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditGlowDisabledSuccess();
    }

}
