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
