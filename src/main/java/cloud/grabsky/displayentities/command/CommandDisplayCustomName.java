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
public enum CommandDisplayCustomName {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> custom_name")
    @CommandPermission("displayentities.command.display.edit.custom_name")
    public String onDisplayCustomName(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @SuggestWith(CustomNameSuggestionProvider.class) String name
    ) {
        // Clearing name if set to @hidden selector.
        if (name.equalsIgnoreCase("@hidden") == true) {
            display.entity().customName(null);
            display.entity().setCustomNameVisible(false);
            // Removing custom name from mannequin's PersistentDataContainer.
            display.remove(DisplayEntities.Keys.MANNEQUIN_CUSTOM_NAME);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditCustomNameSuccess();
        } else {
            display.entity().setRichCustomName(name);
            display.entity().setCustomNameVisible(true);
            // Updating custom name inside mannequin's PersistentDataContainer.
            display.set(DisplayEntities.Keys.MANNEQUIN_CUSTOM_NAME, PersistentDataType.STRING, name);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditCustomNameSuccess();
        }
    }

    public static final class CustomNameSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(final @NotNull ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper.Mannequin argument.
            final @Nullable DisplayWrapper.Mannequin wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.Mannequin.class);
            // If wrapper is not null, returning suggestions based on entity properties.
            if (wrapper != null) {
                final @Nullable String name = wrapper.get(DisplayEntities.Keys.MANNEQUIN_CUSTOM_NAME, PersistentDataType.STRING);
                return (name != null)
                        ? List.of("@hidden", name)
                        : Collections.singletonList("@hidden");
            }
            // Otherwise, returning an empty list.
            return Collections.emptyList();
        }

    }

}
