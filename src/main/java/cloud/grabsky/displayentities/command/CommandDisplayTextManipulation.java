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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;

import static cloud.grabsky.displayentities.util.Conditions.inRange;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayTextManipulation {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    // Display > Edit > Add Line

    @Command("display edit <display> add_line <text>")
    @CommandPermission("displayentities.command.display.edit.add_line")
    public String onDisplayAddLine(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Text display,
            final @NotNull String text
    ) {
        // Getting text contents stored inside PDC of the entity.
        final List<String> contents = getTextContents(display);
        // Setting element at specified index to the provided text.
        contents.add(text);
        // Joining contents to a single string with elements separated using '<newline>' delimiter.
        final String contentsJoined = String.join("<newline>", contents);
        // Updating contents stored in the PDC.
        display.set(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, contentsJoined);
        // Updating contents on the entity.
        display.as(DisplayWrapper.Text.class).entity().setRichText(contentsJoined);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditAddLineSuccess().repl("{number}", contents.size());
    }

    // Display > Edit > Remove Line

    @Command("display edit <display> remove_line <number>")
    @CommandPermission("displayentities.command.display.edit.remove_line")
    public String onDisplayRemoveLine(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Text display,
            final @NotNull @SuggestWith(LineNumberSuggestionProvider.class) Integer number
    ) {
        // Getting text contents stored inside PDC of the entity.
        final List<String> contents = getTextContents(display);
        // Sending error message if user specified index that is out of bounds for the element list.
        if (inRange(number, 1, contents.size()) == false)
            return configuration.messages().commandDisplayEditRemoveLineFailureOutOfBounds().repl("{number}", number).repl("{max}", contents.size());
        // Setting element at specified index to the provided text.
        if (contents.isEmpty() == true)
            return configuration.messages().commandDisplayEditRemoveLineFailureOutOfBounds().repl("{number}", number).repl("{max}", contents.size());
        else contents.remove(number - 1);
        // Joining contents to a single string with elements separated using '<newline>' delimiter.
        final String contentsJoined = String.join("<newline>", contents);
        // Updating contents stored in the PDC.
        display.set(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, contentsJoined);
        // Updating contents on the entity.
        display.as(DisplayWrapper.Text.class).entity().setRichText(contentsJoined);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditRemoveLineSuccess().repl("{number}", number);
    }

    // Display > Edit > Set Line

    @Command("display edit <display> set_line")
    @CommandPermission("displayentities.command.display.edit.set_line")
    public String onDisplaySetLine(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Text display,
            final @NotNull @SuggestWith(LineNumberSuggestionProvider.class) Integer number,
            final @NotNull @SuggestWith(LineTextSuggestionProvider.class) String text
    ) {
        // Getting text contents stored inside PDC of the entity.
        final List<String> contents = getTextContents(display);
        // Sending error message if user specified index that is out of bounds for the element list.
        if (inRange(number, 1, contents.size()) == false)
            return configuration.messages().commandDisplayEditSetLineFailureOutOfBounds().repl("{number}", number).repl("{max}", contents.size());
        // Setting element at specified index to the provided text.
        if (contents.isEmpty() == true)
            contents.add(text);
        else contents.set(number - 1, text);
        // Joining contents to a single string with elements separated using '<newline>' delimiter.
        final String contentsJoined = String.join("<newline>", contents);
        // Updating contents stored in the PDC.
        display.set(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, contentsJoined);
        // Updating contents on the entity.
        display.as(DisplayWrapper.Text.class).entity().setRichText(contentsJoined);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditSetLineSuccess().repl("{number}", number);
    }


    /* HELPER METHODS */

    private static @NotNull ArrayList<String> getTextContents(final @NotNull DisplayWrapper wrapper) {
        return new ArrayList<>() {{
            Collections.addAll(this, wrapper.get(DisplayEntities.Keys.TEXT_CONTENTS, PersistentDataType.STRING, "").split("<newline>"));
        }};
    }


    /* SUGGESTION PROVIDERS */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class LineNumberSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper.Text wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.Text.class);
            // Generating and returning suggestions.
            return (wrapper != null) ? IntStream.range(1, getTextContents(wrapper).size() + 1).mapToObj(String::valueOf).toList() : Collections.emptyList();
        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class LineTextSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper.Text wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.Text.class);
            // Getting the line number argument.
            final @Nullable Integer number = context.getResolvedArgumentOrNull(Integer.class);
            // Returning empty list if any of required arguments is null.
            if (wrapper == null || number == null)
                return Collections.emptyList();
            // Getting text contents stored inside PDC of the entity.
            final List<String> contents = getTextContents(wrapper);
            // Returning selected line in suggestions, or an empty list.
            return (contents.size() >= number) ? Collections.singletonList(contents.get(number - 1)) : Collections.emptyList();
        }

    }

}
