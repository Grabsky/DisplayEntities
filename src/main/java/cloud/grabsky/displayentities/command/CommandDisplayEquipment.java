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
import revxrsal.commands.annotation.Suggest;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayEquipment {
    INSTANCE; // SINGLETON

    @Dependency
    private PluginConfiguration configuration;

    @Command("display edit <display> equipment")
    @CommandPermission("displayentities.command.display.edit.equipment")
    public String onDisplayEquipment(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @Suggest({"head", "chest", "legs", "feet", "hand", "off_hand"}) EquipmentSlot slot,
            final @NotNull ItemType itemType
    ) {
        // Returning when specified slot is not supported.
        if (isSupportedSlot(slot) == false)
            return configuration.messages().commandDisplayEditEquipmentFailureInvalidSlot().repl("{input}", slot.name());;
        // Creating instance of BlockData from provided material.
        final ItemStack item = itemType.createItemStack();
        // Sending error message if BlockData ended up being null.
        if (item.getType().asItemType() == BlockType.AIR)
            return configuration.messages().commandDisplayEditItemFailureSpecifiedInvalidType();
        // Updating entity with new block data.
        display.entity().getEquipment().setItem(slot, item);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditEquipmentSuccess();
    }

    @Command("display edit <display> equipment")
    @CommandPermission("displayentities.command.display.edit.equipment")
    public String onDisplayEquipment(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Mannequin display,
            final @NotNull @Suggest({"head", "chest", "legs", "feet", "hand", "off_hand"}) EquipmentSlot slot,
            final @NotNull @Suggest({"@main_hand", "@off_hand", "@nothing"}) String selector
    ) {
        // Returning when specified slot is not supported.
        if (isSupportedSlot(slot) == false)
            return configuration.messages().commandDisplayEditEquipmentFailureInvalidSlot().repl("{input}", slot.name());
        // Handling '@nothing' selector.
        if (selector.equalsIgnoreCase("@nothing") == true) {
            display.entity().getEquipment().setItem(slot, null);
            return configuration.messages().commandDisplayEditEquipmentSuccess();
        }
        // Handling '@main_hand' and '@off_hand' selectors.
        final @Nullable EquipmentSlot fromSlot = switch (selector) {
            case "@main_hand" -> EquipmentSlot.HAND;
            case "@off_hand" -> EquipmentSlot.OFF_HAND;
            default -> null;
        };
        // Sending error message if specified value is invalid.
        if (fromSlot == null)
            return configuration.messages().errorInvalidRegistryValueItemType().repl("{input}", selector);
        // Getting the item at specified slot.
        final ItemStack item = sender.getInventory().getItem(fromSlot);
        // Sending error message if BlockData ended up being null.
        if (item.getType().asItemType() == BlockType.AIR)
            return configuration.messages().commandDisplayEditEquipmentFailureInvalidItem();
        // Updating entity with new block data.
        display.entity().getEquipment().setItem(slot, item);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditEquipmentSuccess();
    }

    private static boolean isSupportedSlot(final @NotNull EquipmentSlot slot) {
        return switch (slot) {
            // Supported by mannequin entity.
            case HEAD, CHEST, LEGS, FEET, HAND, OFF_HAND -> true;
            // Anything else, while may work, should not be used.
            default -> false;
        };
    }

}
