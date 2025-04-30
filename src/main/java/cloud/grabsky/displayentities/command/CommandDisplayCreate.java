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
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayCreate {
    INSTANCE; // SINGLETON

    @Dependency
    private @UnknownNullability PluginConfiguration configuration;

    private static final Pattern NAME_FORMAT = Pattern.compile("^[a-zA-Z0-9_/:.-]{1,32}$");

    @Command("display create")
    @CommandPermission("displayentities.command.display.create")
    public @NotNull String onDefault(final Player sender) {
        return configuration.messages().commandDisplayCreateUsage();
    }

    @Command("display create")
    @CommandPermission("displayentities.command.display.create")
    public @NotNull String onDisplayCreate(
            final @NotNull Player sender,
            final @NotNull DisplayType type,
            final @NotNull String name
    ) {
        // Sending error message if specified name does not does not match the format.
        if (NAME_FORMAT.matcher(name).matches() == false)
            return configuration.messages().commandDisplayCreateFailureInvalidFormat();
        // Getting player's location and stripping pitch and yaw from it.
        final Location location = sender.getLocation().withPitch(0F).withYaw(0F);
        // Creating, spawning and configuring new display entity.
        final DisplayWrapper display = type.create(location, name).initialConfiguration();
        // Sending success message to the sender.
        return configuration.messages().commandDisplayCreateSuccess().repl("{name}", display.name());
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum DisplayType {
        TEXT(EntityType.TEXT_DISPLAY),
        ITEM(EntityType.ITEM_DISPLAY),
        BLOCK(EntityType.BLOCK_DISPLAY);

        @Getter(AccessLevel.PUBLIC)
        private final EntityType type;

        public DisplayWrapper create(final @NotNull Location location, final @NotNull String name) {
            // Spawning new entity of this type at specified location.
            final Display entity = (Display) location.getWorld().spawnEntity(location, type, SpawnReason.COMMAND);
            // Wrapping and returning...
            return DisplayWrapper.create(entity, name);
        }

    }

}
