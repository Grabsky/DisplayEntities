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
import io.papermc.paper.math.Position;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.SuggestWith;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.node.ExecutionContext;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@SuppressWarnings("UnstableApiUsage") // Position
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayScale {
    INSTANCE; // SINGLETON

    @Dependency
    private @UnknownNullability PluginConfiguration configuration;

    @Command("display edit <display> scale")
    @CommandPermission("displayentities.command.display.edit.scale")
    public String onDefault(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display
    ) {
        return configuration.messages().commandDisplayEditScaleUsage();
    }

    @Command("display edit <display> scale")
    @CommandPermission("displayentities.command.display.edit.scale")
    public String onDisplayScale(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper display,
            // Not really a Position, but it can be easily re-used for this.
            final @NotNull @SuggestWith(ScaleSuggestionProvider.class) Position scale
    ) {
        // Getting current transformation of the display.
        final Transformation transformation = display.entity().getTransformation();
        // Copying transformation with modified scale.
        final Transformation modifiedTransformation = new Transformation(
                transformation.getTranslation(),
                transformation.getLeftRotation(),
                new Vector3f((float) scale.x(), (float) scale.y(), (float) scale.z()),
                transformation.getRightRotation()
        );
        // Updating entity with new transformation.
        display.entity().setTransformation(modifiedTransformation);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditScaleSuccess().repl("{x}", scale.x()).repl("{y}", scale.y()).repl("{z}", scale.z());
    }

    /* SUGGESTION PROVIDERS */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ScaleSuggestionProvider implements SuggestionProvider<BukkitCommandActor> {

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            // Returning empty list if wrapper was unspecified.
            if (wrapper == null)
                return Collections.emptyList();
            // Getting the scale.
            final Vector3f scale = wrapper.entity().getTransformation().getScale();
            // Generating and returning suggestions.
            return Collections.singletonList(String.format("%.2f %.2f %.2f", scale.x, scale.y, scale.z));
        }

    }

}
