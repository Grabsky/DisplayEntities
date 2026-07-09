/*
 * DisplayEntities (https://github.com/Grabsky/DisplayEntities)
 *
 * Copyright (C) 2026  Grabsky <michal.czopek.foss@proton.me>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License v3 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License v3 for more details.
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
