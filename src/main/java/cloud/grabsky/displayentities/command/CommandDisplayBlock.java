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
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.ExtensionMethod;

@SuppressWarnings("UnstableApiUsage") // Item Components API
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayBlock {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> block")
    @CommandPermission("displayentities.command.display.edit.block")
    public String onDisplayBlock(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Block display,
            final @NotNull BlockType blockType
    ) {
        // Creating instance of BlockData from provided BlockType.
        final BlockData data = blockType.createBlockData();
        // Sending error message if BlockData ended up being null.
        if (data.getMaterial().asBlockType() == BlockType.AIR)
            return configuration.messages().commandDisplayEditBlockFailureSpecifiedInvalidType();
        // Updating entity with new block data.
        display.as(DisplayWrapper.Block.class).entity().setBlock(data);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditBlockSuccess().repl("{type}", data.getMaterial().key().asString());
    }

    @Command("display edit <display> block")
    @CommandPermission("displayentities.command.display.edit.block")
    public String onDisplayBlock(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Block display,
            final @NotNull @Suggest({"@main_hand", "@off_hand"}) String selector
    ) {
        // Getting the specified slot.
        final @Nullable EquipmentSlot slot = switch (selector) {
            case "@main_hand" -> EquipmentSlot.HAND;
            case "@off_hand" -> EquipmentSlot.OFF_HAND;
            default -> null;
        };
        // Sending error message if specified value is invalid.
        if (slot == null)
            return configuration.messages().errorInvalidRegistryValueBlockType().repl("{input}", selector);
        // Getting the item at specified slot.
        final ItemStack item = sender.getInventory().getItem(slot);
        // Creating instance of BlockData from item at specified slot.
        final BlockData data = (item.getType().asBlockType() != null)
                // Returning new block data from the currently held item.
                ? (item.hasData(DataComponentTypes.BLOCK_DATA) == true)
                        // Checking for custom BlockData and creating from that if exists.
                        ? item.getData(DataComponentTypes.BLOCK_DATA).createBlockData(item.getType().asBlockType())
                        // Otherwise, creating from the block type.
                        : item.getType().asBlockType().createBlockData()
                // Otherwise, returning null.
                : null;
        // Sending error message if BlockData ended up being null.
        if (data == null || data.getMaterial().asBlockType() == BlockType.AIR)
            return configuration.messages().commandDisplayEditBlockFailureHoldingInvalidType();
        // Updating entity with new block data.
        display.as(DisplayWrapper.Block.class).entity().setBlock(data);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditBlockSuccess().repl("{type}", data.getMaterial().key().asString());
    }

}
