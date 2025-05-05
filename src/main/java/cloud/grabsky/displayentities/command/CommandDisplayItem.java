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
import org.bukkit.block.BlockType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.ExtensionMethod;

@SuppressWarnings("UnstableApiUsage") // ItemType
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayItem {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> item")
    @CommandPermission("displayentities.command.display.edit.item")
    public String onDisplayItem(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Item display,
            final @NotNull ItemType itemType
    ) {
        // Creating instance of BlockData from provided material.
        final ItemStack item = itemType.createItemStack();
        // Sending error message if BlockData ended up being null.
        if (item.getType().asItemType() == BlockType.AIR)
            return configuration.messages().commandDisplayEditItemFailureSpecifiedInvalidType();
        // Updating entity with new block data.
        display.as(DisplayWrapper.Item.class).entity().setItemStack(item);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditItemSuccess().repl("{type}", item.getType().key().asString());
    }

    @Command("display edit <display> item")
    @CommandPermission("displayentities.command.display.edit.item")
    public String onDisplayItem(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Item display,
            final @NotNull String selector
    ) {
        // Getting the specified slot.
        final @Nullable EquipmentSlot slot = switch (selector) {
            case "@main_hand" -> EquipmentSlot.HAND;
            case "@off_hand" -> EquipmentSlot.OFF_HAND;
            default -> null;
        };
        // Sending error message if specified value is invalid.
        if (slot == null)
            return configuration.messages().errorInvalidRegistryValueItemType().repl("{input}", selector);
        // Getting the item at specified slot.
        final ItemStack item = sender.getInventory().getItem(slot);
        // Sending error message if BlockData ended up being null.
        if (item.getType().asItemType() == BlockType.AIR)
            return configuration.messages().commandDisplayEditBlockFailureHoldingInvalidType();
        // Updating entity with new block data.
        display.as(DisplayWrapper.Item.class).entity().setItemStack(item);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditItemSuccess().repl("{type}", item.getType().key().asString());
    }

}
