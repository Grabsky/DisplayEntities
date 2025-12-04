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
import cloud.grabsky.displayentities.util.Conditions;
import cloud.grabsky.displayentities.util.LombokExtensions;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

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
            final @NotNull String skin
    ) {
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
