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
            final @Nullable @Optional @Suggest({"100%", "50%", "25%", "0%"}) String transparency
    ) {
        // Calculating the transparency value if specified. It converts percentage value to an integer between 0 - 255.
        final Color finalColor = (transparency != null) ? color.setAlpha((int) Math.round(Math.clamp(Float.parseFloat(transparency.repl("%", "")), 0D, 100D) * 255 / 100)) : color;
        // Updating value of the background_color property of the display entity.
        display.entity().setBackgroundColor(finalColor);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditBackgroundSuccess().repl("{color}", "#" + Integer.toHexString(finalColor.asARGB()).toUpperCase());
    }

}
