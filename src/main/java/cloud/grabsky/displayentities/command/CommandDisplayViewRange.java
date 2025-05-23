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
import revxrsal.commands.annotation.SuggestWith;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.node.ExecutionContext;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayViewRange {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> view_range")
    @CommandPermission("displayentities.command.display.edit.view_range")
    public String onDisplayViewRange(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display,
            final @NotNull @SuggestWith(ViewDistanceSuggestionProvider.class) Float viewRange
    ) {
        // Calculating the view range value. Cannot be less than 0.
        final float finalViewRange = Math.max(0F, viewRange);
        // Updating value of the view_range property of the display entity.
        display.entity().setViewRange(finalViewRange);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditViewRangeSuccess().repl("{range}", finalViewRange);
    }

    /* SUGGESTION PROVIDERS */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ViewDistanceSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Generating and returning suggestions.
            return (wrapper != null) ? Collections.singletonList(String.format("%.2f", wrapper.entity().getViewRange())) : Collections.emptyList();
        }

    }

}
