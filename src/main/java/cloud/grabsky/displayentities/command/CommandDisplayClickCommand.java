/*
 * DisplayEntities (https://github.com/Grabsky/DisplayEntities)
 *
 * MIT License
 *
 * Copyright (c) 2026 Grabsky (michal.czopek.foss@proton.me)
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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

public enum CommandDisplayClickCommand {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> click_command <command>")
    @CommandPermission("displayentities.command.display.edit.click_command")
    public String onDisplayClickCommand(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Interaction display,
            final @NotNull @SuggestWith(ClickCommandSuggestionProvider.class) String command
    ) {
        // When command is specified as '@none', click command is removed from data container.
        if (command.equalsIgnoreCase("@none") == true) {
            display.remove(DisplayEntities.Keys.CLICK_COMMAND);
            return configuration.messages().commandDisplayEditClickCommandSuccess();
        }
        // Otherwise, setting / updating click command.
        display.set(DisplayEntities.Keys.CLICK_COMMAND, PersistentDataType.STRING, command);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditClickCommandSuccess();
    }

    @Command("display edit <display> click_command <command>")
    @CommandPermission("displayentities.command.display.edit.click_command")
    public String onDisplayClickCommand(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @SuggestWith(ClickCommandSuggestionProvider.class) String command
    ) {
        // When command is specified as '@none', click command is removed from data container.
        if (command.equalsIgnoreCase("@none") == true) {
            display.remove(DisplayEntities.Keys.CLICK_COMMAND);
            return configuration.messages().commandDisplayEditClickCommandSuccess();
        }
        // Otherwise, setting / updating click command.
        display.set(DisplayEntities.Keys.CLICK_COMMAND, PersistentDataType.STRING, command);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditClickCommandSuccess();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ClickCommandSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(final @NotNull ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Generating and returning suggestions.
            return (wrapper != null)
                    ? List.of("@none", wrapper.get(DisplayEntities.Keys.CLICK_COMMAND, PersistentDataType.STRING, ""))
                    : Collections.singletonList("@none");
        }

    }

}
