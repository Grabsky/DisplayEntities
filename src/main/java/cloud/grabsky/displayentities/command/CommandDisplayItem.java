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
