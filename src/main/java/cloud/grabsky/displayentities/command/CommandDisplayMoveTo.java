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
import io.papermc.paper.math.Position;
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
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("UnstableApiUsage") // Position
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayMoveTo {
    INSTANCE; // SINGLETON

    @Dependency
    private @UnknownNullability PluginConfiguration configuration;

    @Command("display edit <display> move_to")
    @CommandPermission("displayentities.command.display.edit.move_to")
    public String onDisplayMoveToDefault(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display
    ) {
        return configuration.messages().commandDisplayEditMoveToUsage();
    }


    @Command("display edit <display> move_to")
    @CommandPermission("displayentities.command.display.edit.move_to")
    public String onDisplayMoveTo(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display,
            final @NotNull @SuggestWith(CurrentPositionSuggestionProvider.class) Position position
    ) {
        // Teleporting entity to desired location.
        display.entity().teleportAsync(position.toLocation(display.entity().getWorld()));
        // Returning (sending) success message to the sender.
        return configuration.messages().commandDisplayEditMoveToSuccess()
                .repl("{x}", String.format("%.2f", position.x()))
                .repl("{y}", String.format("%.2f", position.y()))
                .repl("{z}", String.format("%.2f", position.z()));
    }

    /* SUGGESTION PROVIDERS */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class CurrentPositionSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Returning empty completions if wrapper is null.
            return (wrapper != null)
                    ? List.of("~ ~ ~", String.format("%.2f %.2f %.2f", wrapper.entity().getX(), wrapper.entity().getY(), wrapper.entity().getZ()))
                    : Collections.emptyList();
        }

    }

}
