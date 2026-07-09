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

import cloud.grabsky.displayentities.DisplayWrapper;
import cloud.grabsky.displayentities.configuration.PluginConfiguration;
import cloud.grabsky.displayentities.util.Conditions;
import cloud.grabsky.displayentities.util.LombokExtensions;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Switch;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.ExtensionMethod;

// TO-DO: Support for NamespacedKey texture field and maybe remaining properties like cape, elytra etc.
//        Maybe PlaceholderAPI support? Not needed for initial release.
@SuppressWarnings("UnstableApiUsage")
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplaySkin {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> skin")
    @CommandPermission("displayentities.command.display.edit.skin")
    public String onDisplaySkin(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull String skin,
            final @Switch("slim") boolean isSlim
    ) {
        // Handling texture URLs.
        if (skin.startsWith("https://textures.minecraft.net/texture/") == true) {
            // Creating dummy profile and getting textures.
            final PlayerProfile dummyProfile = Bukkit.createProfile(UUID.randomUUID());
            final PlayerTextures textures = dummyProfile.getTextures();
            try {
                textures.setSkin(URI.create(skin).toURL(), (isSlim == true) ? PlayerTextures.SkinModel.SLIM : PlayerTextures.SkinModel.CLASSIC);
                dummyProfile.setTextures(textures);
                // Getting the full skin identifier from provided URL.
                final String fullId = skin.substring(skin.lastIndexOf("/") + 1);
                // Setting the (skin) profile of the mannequin entity.
                display.entity().setProfile(ResolvableProfile.resolvableProfile(dummyProfile));
                // Sending success message to the sender.
                return configuration.messages().commandDisplayEditSkinSuccess()
                        .repl("{skin}", fullId.substring(0, 8) + "..." + fullId.substring(fullId.length() - 4));
            } catch (final IllegalArgumentException | MalformedURLException e) {
                // Sending failure message to the sender.
                return configuration.messages().commandDisplayEditSkinFailureInvalidUrl();
            }
        } else {
            // Creating ResolvableProfile based on user input.
            final ResolvableProfile profile = (Conditions.isUUID(skin) == true)
                    ? ResolvableProfile.resolvableProfile(Bukkit.createProfile(UUID.fromString(skin)))
                    : ResolvableProfile.resolvableProfile(Bukkit.createProfile(skin));
            // Setting the (skin) profile of the mannequin entity.
            display.entity().setProfile(profile);
            // Sending success message to the sender.
            return configuration.messages().commandDisplayEditSkinSuccess().repl("{skin}", skin);
        }
    }

    private static boolean isTextureURL(final @NotNull String string) {
        return string.startsWith("https://") || string.startsWith("http://");
    }

}
