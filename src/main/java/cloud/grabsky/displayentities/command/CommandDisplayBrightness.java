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
import org.bukkit.entity.Display;
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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayBrightness {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> brightness block")
    @CommandPermission("displayentities.command.display.edit.brightness")
    public String onDisplayBrightnessBlock(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Strict display,
            final @NotNull @SuggestWith(BrightnessSuggestionProvider.Block.class) Integer brightness
    ) {
        // Calculating the brightness value. Must be in between 0 and 15.
        final int finalBrightness = Math.clamp(brightness, 0, 15);
        // Creating new Display.Brightness object.
        final Display.Brightness newBrightness = new Display.Brightness(finalBrightness, display.entity(Display.class).getBrightness() != null ? display.entity(Display.class).getBrightness().getSkyLight() : 15);
        // Updating value of the brightness property of the display entity.
        display.entity(Display.class).setBrightness(newBrightness);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditBrightnessSuccess().repl("{brightness_block}", newBrightness.getBlockLight()).repl("{brightness_sky}", newBrightness.getSkyLight());
    }

    @Command("display edit <display> brightness sky")
    @CommandPermission("displayentities.command.display.edit.brightness")
    public String onDisplayBrightnessSky(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Strict display,
            final @NotNull @SuggestWith(BrightnessSuggestionProvider.Sky.class) Integer brightness
    ) {
        // Calculating the brightness value. Must be in between 0 and 15.
        final int finalBrightness = Math.clamp(brightness, 0, 15);
        // Creating new Display.Brightness object.
        final Display.Brightness newBrightness = new Display.Brightness(display.entity(Display.class).getBrightness() != null ? display.entity(Display.class).getBrightness().getBlockLight() : 15, finalBrightness);
        // Updating value of the brightness property of the display entity.
        display.entity(Display.class).setBrightness(newBrightness);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditBrightnessSuccess().repl("{brightness_block}", newBrightness.getBlockLight()).repl("{brightness_sky}", newBrightness.getSkyLight());
    }

    /* SUGGESTION PROVIDERS */

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static sealed class BrightnessSuggestionProvider implements SuggestionProvider<BukkitCommandActor> permits BrightnessSuggestionProvider.Block, BrightnessSuggestionProvider.Sky {

        private final boolean isBlockBrightness;

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Returning empty suggestions list if wrapper was not specified.
            if (wrapper == null)
                return Collections.emptyList();
            // Getting current brightness.
            final @Nullable Display.Brightness brightness = wrapper.entity(Display.class).getBrightness();
            // Generating and returning suggestions.
            return (brightness != null)
                    ? Collections.singletonList("" + (isBlockBrightness == true ? brightness.getBlockLight() : brightness.getSkyLight()))
                    : List.of("0", "15");
        }

        // BrightnessSuggestionProvider instance that generates suggestion for the current block light override.
        public static final class Block extends BrightnessSuggestionProvider {

            public Block() {
                super(true);
            }

        }

        // BrightnessSuggestionProvider instance that generates suggestion for the current sky light override.
        public static final class Sky extends BrightnessSuggestionProvider {

            public Sky() {
                super(false);
            }

        }

    }

}
