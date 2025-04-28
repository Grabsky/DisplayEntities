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
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayViewRange {
    INSTANCE; // SINGLETON

    @Dependency
    private @UnknownNullability PluginConfiguration configuration;

    @Command("display edit <display> view_range")
    @CommandPermission("displayentities.command.display.edit.view_range")
    public String onDefault(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display
    ) {
        return configuration.messages().commandDisplayEditViewRangeUsage();
    }

    @Command("display edit <display> view_range")
    @CommandPermission("displayentities.command.display.edit.view_range")
    public String onDisplayItem(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display,
            final @Nullable @Optional @Suggest("1.0") Float viewRange
    ) {
        // Calculating the view range value. Cannot be less than 0.
        final float finalViewRange = Math.max(0F, (viewRange != null) ? viewRange : 0F);
        // Setting view range on provided display entity.
        display.entity().setViewRange(finalViewRange);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditViewRangeSuccess().repl("{range}", finalViewRange);
    }

}
