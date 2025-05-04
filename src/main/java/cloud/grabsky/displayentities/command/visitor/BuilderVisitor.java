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
package cloud.grabsky.displayentities.command.visitor;

import cloud.grabsky.displayentities.DisplayEntities;
import cloud.grabsky.displayentities.DisplayWrapper;
import cloud.grabsky.displayentities.command.parameter.ColorParameterType;
import cloud.grabsky.displayentities.command.parameter.DisplayWrapperParameterType;
import cloud.grabsky.displayentities.command.parameter.PositionParameterType;
import cloud.grabsky.displayentities.command.parameter.RegistryParameterType;
import cloud.grabsky.displayentities.util.LombokExtensions;
import io.papermc.paper.math.Position;
import org.bukkit.Color;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import revxrsal.commands.Lamp;
import revxrsal.commands.LampBuilderVisitor;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.exception.BukkitExceptionHandler;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;
import revxrsal.commands.command.Potential;
import revxrsal.commands.exception.EnumNotFoundException;
import revxrsal.commands.exception.ExpectedLiteralException;
import revxrsal.commands.exception.InvalidBooleanException;
import revxrsal.commands.exception.InvalidDecimalException;
import revxrsal.commands.exception.InvalidIntegerException;
import revxrsal.commands.exception.NoPermissionException;
import revxrsal.commands.node.FailureHandler;
import revxrsal.commands.stream.StringStream;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class BuilderVisitor implements LampBuilderVisitor<BukkitCommandActor> {

    @Getter(AccessLevel.PUBLIC)
    private final DisplayEntities plugin;

    @Override @SuppressWarnings({"unchecked", "UnstableApiUsage"}) // Position, ItemType, BlockType
    public void visit(final @NotNull Lamp.Builder<BukkitCommandActor> builder) {
        // Registering custom parameter types.
        builder.parameterTypes(it -> {
            it.addParameterType(Color.class, ColorParameterType.INSTANCE);
            it.addParameterType(Position.class, PositionParameterType.INSTANCE);
            it.addParameterType(ItemType.class, new RegistryParameterType<>(ItemType.class, () -> Registry.ITEM));
            it.addParameterType(BlockType.class, new RegistryParameterType<>(BlockType.class, () -> Registry.BLOCK));
            it.addParameterTypeFactory(DisplayWrapperParameterType.INSTANCE);
        });
        // Setting the failure handler.
        builder.dispatcherSettings().failureHandler(new CustomFailureHandler<>());
        // Setting the exception handler.
        builder.exceptionHandler(new CustomExceptionHandler());
        // Registering command response handler for String object.
        builder.responseHandler(String.class, (message, context) -> {
            if (message.isEmpty() == false)
                context.actor().reply(plugin.miniMessage().deserialize(message));
        });
        // Registering command response handler for String object.
        builder.responseHandler(List.class, (list, context) -> {
            if (list.isEmpty() == false && list.getFirst() instanceof String) {
                // Should be safe to assume it's a list of String objects.
                final List<String> messages = (List<String>) list;
                // Joining on <newline> and sending.
                context.actor().reply(plugin.miniMessage().deserialize(String.join("<newline>", messages)));
            }
        });
    }

    /* CUSTOM EXCEPTION HANDLER */

    // Anything that isn't handled here will revert to Lamp defaults.
    public final class CustomExceptionHandler extends BukkitExceptionHandler {

        @Override
        public void onNoPermission(final @NotNull NoPermissionException e, final @NotNull BukkitCommandActor actor) {
            actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorMissingPermission()));
        }

        @Override
        public void onSenderNotPlayer(final @NotNull SenderNotPlayerException e, final @NotNull BukkitCommandActor actor) {
            actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorSenderNotPlayer()));
        }

        @Override
        public void onEnumNotFound(final @NotNull EnumNotFoundException e, final @NotNull BukkitCommandActor actor) {
            if (e.enumType() == DisplayWrapper.Type.class)
                actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorEnumNotFoundDisplayType().repl("{input}", e.input())));
        }

        @Override
        public void onInvalidBoolean(final @NotNull InvalidBooleanException e, final @NotNull BukkitCommandActor actor) {
            actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorInvalidBoolean().repl("{input}", e.input())));
        }

        @Override
        public void onInvalidInteger(final @NotNull InvalidIntegerException e, final @NotNull BukkitCommandActor actor) {
            actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorInvalidInteger().repl("{input}", e.input())));
        }

        @Override
        public void onInvalidDecimal(final @NotNull InvalidDecimalException e, final @NotNull BukkitCommandActor actor) {
            actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorInvalidDecimal().repl("{input}", e.input())));
        }

        @HandleException
        public void onInvalidDisplayWrapper(final @NotNull DisplayWrapperParameterType.Exception e, final @NotNull BukkitCommandActor actor) {
            actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorInvalidDisplay().repl("{input}", e.input())));
        }

        @HandleException @SuppressWarnings("UnstableApiUsage") // Position, ItemType, BlockType
        public void onInvalidRegistryValue(final @NotNull RegistryParameterType.Exception e, final @NotNull BukkitCommandActor actor) {
            // Sending specialized error for the ItemType registry.
            if (e.registryType() == ItemType.class)
                actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorInvalidRegistryValueItemType().repl("{input}", e.input())));
            // Sending specialized error for the BlockType registry.
            else if (e.registryType() == BlockType.class)
                actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorInvalidRegistryValueBlockType().repl("{input}", e.input())));
            // Fallback to a safety net in case of an unhandled registry type.
            else actor.reply(plugin.miniMessage().deserialize("<red>Invalid value for " + e.registryType().getSimpleName() + " registry."));
        }

    }

    /* CUSTOM FAILURE HANDLER */

    // Modified from the original DefaultFailureHandler provided by Lamp.
    // This one does not print list of available commands, but instead sends a customizable message.
    public final class CustomFailureHandler<A extends BukkitCommandActor> implements FailureHandler<A> {

        @Override
        public void handleFailedAttempts(final @NotNull A actor, final @NotNull @Unmodifiable List<Potential<A>> failedAttempts, final @NotNull StringStream input) {
            // Returning if there were no failed attempts.
            if (failedAttempts.isEmpty() == true)
                return;
            // Otherwise, if one exception was thrown, handling it.
            else if (failedAttempts.size() == 1) {
                failedAttempts.getFirst().handleException();
                return;
            }
            // Filtering ExpectedLiteralException to make errors a bit more accurate.
            final var filteredExceptions = failedAttempts.stream().filter(failedAttempt -> failedAttempt instanceof ExpectedLiteralException == false).toList();
            // Replying with generic error for all non-existent commands.
            if (filteredExceptions.isEmpty() == true)
                actor.reply(plugin.miniMessage().deserialize(plugin.configuration().messages().errorInvalidCommand()));
            // Otherwise, letting exception handler do it's thing.
            else filteredExceptions.getFirst().handleException();
        }

    }

}
