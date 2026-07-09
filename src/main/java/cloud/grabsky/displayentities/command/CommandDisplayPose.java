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
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

// TO-DO: Sitting pose requires a better implementation.
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayPose {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> pose")
    @CommandPermission("displayentities.command.display.edit.pose")
    public String onDisplayPose(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @Suggest({"standing", "sneaking", "swimming", "fall_flying", "sleeping"}) Pose pose
    ) {
        // Returning when specified pose is not supported.
        if (isSupportedPose(pose) == false)
            return configuration.messages().errorEnumNotFoundPose().repl("{input}", pose.name().toLowerCase());
        // Setting the new pose.
        display.entity().setPose(pose);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditPoseSuccess().repl("{pose}", pose.name().toLowerCase());
    }

    private static boolean isSupportedPose(final @NotNull Pose pose) {
        return switch (pose) {
            // Supported by default.
            case STANDING, SNEAKING, SWIMMING, FALL_FLYING, SLEEPING -> true;
            // Anything else is not supported.
            default -> false;
        };
    }

}
