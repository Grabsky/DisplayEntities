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
import cloud.grabsky.displayentities.util.LombokExtensions;
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
            return configuration.messages().errorEnumNotFoundEquipmentSlot().repl("{input}", slot.name());;
        // Creating instance of ItemStack from provided item type.
        final ItemStack item = itemType.createItemStack();
        // Sending error message ItemStack ends up being empty.
        if (item.isEmpty() == true)
            return configuration.messages().commandDisplayEditItemFailureSpecifiedInvalidType().repl("{input}", itemType.key().asString());
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
            return configuration.messages().errorEnumNotFoundEquipmentSlot().repl("{input}", slot.name());
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
        // Sending error message ItemStack is empty.
        if (item.isEmpty() == true)
            return configuration.messages().errorInvalidRegistryValueItemType().replace("{input}", item.getType().key().asString());
        // Updating equipment on the mannequin entity.
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
