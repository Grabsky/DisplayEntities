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

import cloud.grabsky.displayentities.DisplayWrapper;
import cloud.grabsky.displayentities.configuration.PluginConfiguration;
import cloud.grabsky.displayentities.util.LombokExtensions;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Display;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.ParseWith;
import revxrsal.commands.annotation.SuggestWith;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayScale {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> scale")
    @CommandPermission("displayentities.command.display.edit.scale")
    public String onDisplayScale(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Strict display,
            final @NotNull @ParseWith(ScaleParameterType.class) @SuggestWith(ScaleParameterType.class) Vector3f scale
    ) {
        // Getting the current transformation of the display.
        final Transformation transformation = display.entity(Display.class).getTransformation();
        // Copying transformation with a modified scale.
        final Transformation modifiedTransformation = new Transformation(
                transformation.getTranslation(),
                transformation.getLeftRotation(),
                scale,
                transformation.getRightRotation()
        );
        // Updating entity with a new transformation.
        display.entity(Display.class).setTransformation(modifiedTransformation);
        // Sending a success message to the sender.
        return configuration.messages().commandDisplayEditScaleSuccess().repl("{x}", scale.x).repl("{y}", scale.y).repl("{z}", scale.z);
    }

    @Command("display edit <display> scale")
    @CommandPermission("displayentities.command.display.edit.scale")
    public String onDisplayScale(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @SuggestWith(ScaleParameterType.class) Float scale
    ) {
        // Setting the scale attribute. Mannequins have this attribute registered by default, so NPE should generally not be thrown.
        display.entity(Mannequin.class).getAttribute(Attribute.SCALE).setBaseValue(scale);

        // Sending a success message to the sender.
        return configuration.messages().commandDisplayEditScaleSuccessMannequin().repl("{scale}", scale);
    }

    /* PARAMETER PARSER */

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ScaleParameterType implements ParameterType<BukkitCommandActor, Vector3f>, SuggestionProvider<BukkitCommandActor> {

        @Override
        public Vector3f parse(final @NotNull MutableStringStream input, final @NotNull ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            if (wrapper == null)
                return null;
            // Getting the scale around X axis. Consuming the next space if it exists.
            final float x = (input.hasRemaining() == true) ? input.readFloat() : wrapper.entity(Display.class).getTransformation().getScale().x;
            if (input.hasRemaining() == true && input.peek() == ' ')
                input.moveForward();
            // Getting the scale around Y axis. Consuming the next space if it exists.
            final float y = (input.hasRemaining() == true) ? input.readFloat() : wrapper.entity(Display.class).getTransformation().getScale().y;
            if (input.hasRemaining() == true && input.peek() == ' ')
                input.moveForward();
            // Getting the scale around Z axis.
            final float z = (input.hasRemaining() == true) ? input.readFloat() : wrapper.entity(Display.class).getTransformation().getScale().z;
            // Returning the value.
            return new Vector3f(x, y, z);
        }

        @Override
        public @NotNull Collection<String> getSuggestions(@NotNull final ExecutionContext<BukkitCommandActor> context) {
            // Getting the DisplayWrapper argument.
            final @Nullable DisplayWrapper wrapper = context.getResolvedArgumentOrNull(DisplayWrapper.class);
            if (wrapper instanceof DisplayWrapper.Strict) {
                // Getting the scale.
                final Vector3f scale = wrapper.entity(Display.class).getTransformation().getScale();
                // Generating and returning suggestions.
                return Collections.singletonList(String.format("%.2f %.2f %.2f", scale.x, scale.y, scale.z));
            } else if (wrapper instanceof DisplayWrapper.Mannequin) {
                // Generating and returning suggestions. Scale attribute should always be registered for Mannequin entities, thus making NPE unlikely to be thrown.
                return Collections.singletonList(String.format("%.2f", wrapper.entity(Mannequin.class).getAttribute(Attribute.SCALE).getBaseValue()));
            }
            // Returning an empty list if the wrapper was unspecified.
            return Collections.emptyList();
        }

    }

}
