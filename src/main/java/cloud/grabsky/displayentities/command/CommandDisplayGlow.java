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
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;

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

}
