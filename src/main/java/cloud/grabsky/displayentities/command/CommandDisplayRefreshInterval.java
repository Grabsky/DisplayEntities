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

import cloud.grabsky.displayentities.DisplayEntities;
import cloud.grabsky.displayentities.DisplayWrapper;
import cloud.grabsky.displayentities.configuration.PluginConfiguration;
import cloud.grabsky.displayentities.util.LombokExtensions;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
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

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayRefreshInterval {
    INSTANCE; // SINGLETON

    @Dependency
    private DisplayEntities plugin;

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> refresh_interval")
    @CommandPermission("displayentities.command.display.edit.refresh_interval")
    public String onDisplayAlignment(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Text display,
            final @NotNull @SuggestWith(IntervalSuggestionProvider.class) String interval
    ) {
        if (interval.equals("default") == true) {
            // Removing the stored refresh_interval so it takes the config value instead.
            display.remove(DisplayEntities.Keys.REFRESH_INTERVAL);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditRefreshIntervalSuccess().repl("{ticks}", configuration.refreshInterval());
        }
        final @Nullable Integer parsedInterval = parseInt(interval);
        // Sending error message if invalid value was found.
        if (parsedInterval == null)
            return configuration.messages().commandDisplayEditRefreshIntervalFailure();
        // Setting refresh_interval of this display entity.
        display.set(DisplayEntities.Keys.REFRESH_INTERVAL, PersistentDataType.INTEGER, Math.max(1, parsedInterval));
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditRefreshIntervalSuccess().repl("{ticks}", Math.max(1, parsedInterval));
    }

    /* HELPER METHODS */

    private static @Nullable Integer parseInt(final @NotNull String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /* SUGGESTION PROVIDER */

    public static final class IntervalSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(final @NotNull ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Returning empty list if wrapper was unspecified.
            return (wrapper != null)
                    ? (wrapper.get(DisplayEntities.Keys.REFRESH_INTERVAL, PersistentDataType.INTEGER) != null)
                            ? List.of("" + wrapper.get(DisplayEntities.Keys.REFRESH_INTERVAL, PersistentDataType.INTEGER), "default")
                            : Collections.singletonList("default")
                    : Collections.emptyList();
        }

    }

}
