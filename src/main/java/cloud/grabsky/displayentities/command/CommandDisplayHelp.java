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
import cloud.grabsky.displayentities.configuration.PluginConfiguration;
import cloud.grabsky.displayentities.util.LombokExtensions;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.SuggestWith;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.node.ExecutionContext;

import java.util.Collection;
import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayHelp {
    INSTANCE; // SINGLETON

    @Dependency
    private DisplayEntities plugin;

    @Dependency
    private PluginConfiguration configuration;

    @Command("display help")
    @CommandPermission("displayentities.command.display.help")
    public void onDefault(
            final @NotNull CommandSender sender,
            final @Nullable @Optional @SuggestWith(PageSuggestionProvider.class) Integer page
    ) {
        // Calculating max page number.
        final int maxPage = Math.max(1, (int) Math.ceil(configuration.messages().commandDisplayHelpContents().size() / 6.0D));
        // Getting the requested page. Defaults to 1 for invalid input and is capped by number of the last page.
        final int finalPage = (page != null) ? Math.clamp(page, 1, maxPage) : 1;
        // Sending help header.
        if (configuration.messages().commandDisplayHelpHeader().isEmpty() == false)
            sender.sendMessage(plugin.miniMessage().deserialize(String.join("<newline>", configuration.messages().commandDisplayHelpHeader()).repl("{page}", finalPage, "{max_page}", maxPage)));
        // Calculating contents of the requested page and sending.
        sender.sendMessage(plugin.miniMessage().deserialize(String.join("<newline>", configuration.messages().commandDisplayHelpContents().stream().skip((finalPage - 1) * 6L).limit(6).toList())));
        // Sending help footer.
        if (configuration.messages().commandDisplayHelpFooter().isEmpty() == false)
            sender.sendMessage(plugin.miniMessage().deserialize(String.join("<newline>", configuration.messages().commandDisplayHelpFooter()).repl("{page}", finalPage, "{max_page}", maxPage)));
    }

    /* SUGGESTION PROVIDERS */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PageSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            final int maxPage = Math.max(1, (int) Math.ceil(DisplayEntities.instance().configuration().messages().commandDisplayHelpContents().size() / 6.0D));
            // Generating suggestions and returning.
            return IntStream.range(1, maxPage + 1).mapToObj(String::valueOf).toList();
        }

    }

}
