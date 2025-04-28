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
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import lombok.experimental.ExtensionMethod;

@SuppressWarnings("UnstableApiUsage") // BlockType
@ExtensionMethod(LombokExtensions.class)
public enum CommandDisplayBlock {
    INSTANCE; // SINGLETON

    @Dependency
    private @UnknownNullability PluginConfiguration configuration;

    @Command("display edit <display> block")
    @CommandPermission("displayentities.command.display.edit.block")
    public String onDisplayBlock(
            final @NotNull Player sender,
            final @NotNull DisplayWrapper.Block display,
            final @Nullable @Optional BlockType blockType
    ) {
        // Creating instance of BlockData from provided BlockType, or item held by the player.
        final BlockData data = (blockType != null)
                ? blockType.createBlockData()
                // Making sure currently held item is (1) of block type (2) have block_data component.
                : (sender.getInventory().getItemInMainHand().getType().asBlockType() != null) && (sender.getInventory().getItemInMainHand().getData(DataComponentTypes.BLOCK_DATA) != null)
                        // Returning new block data from the currently held item.
                        ? sender.getInventory().getItemInMainHand().getData(DataComponentTypes.BLOCK_DATA).createBlockData(sender.getInventory().getItemInMainHand().getType().asBlockType())
                        // Otherwise, returning null.
                        : null;
        // Sending error message if BlockData ended up being null.
        if (data == null || data.getMaterial().asBlockType() == BlockType.AIR)
            return configuration.messages().commandDisplayEditBlockFailure();
        // Updating entity with new block data.
        display.as(DisplayWrapper.Block.class).entity().setBlock(data);
        // Sending success message to the sender.
        return configuration.messages().commandDisplayEditBlockSuccess().repl("{type}", data.getMaterial().key().asString());
    }

}
