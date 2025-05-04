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
package cloud.grabsky.displayentities.configuration;

import cloud.grabsky.displayentities.DisplayEntities;
import revxrsal.spec.annotation.Comment;
import revxrsal.spec.annotation.ConfigSpec;
import revxrsal.spec.annotation.Key;
import revxrsal.spec.annotation.Order;
import revxrsal.spec.annotation.Reload;
import revxrsal.spec.annotation.Reset;
import revxrsal.spec.annotation.Save;

import java.util.LinkedHashMap;
import java.util.List;

import org.jetbrains.annotations.ApiStatus.NonExtendable;

@ConfigSpec
public interface PluginConfiguration {

    @Order(0) @Key("debug_mode")
    @Comment("Debug mode prints additional information to the console. (Default: false)")
    default boolean debugMode() {
        return false;
    }

    @Order(1) @Key("refresh_interval")
    @Comment("Placeholders refresh interval in ticks. (Default: 200)")
    default int refreshInterval() {
        return 200;
    }

    @Order(2) @Key("predefined_colors")
    @Comment("Predefined colors for use in messages section of the configuration file.")
    default LinkedHashMap<String, String> predefinedColors() {
        return new LinkedHashMap<>() {{
            put("primary", "#65D85F");
            put("secondary", "#C7F1C5");
        }};
    }

    @Order(3) @Key("messages")
    @Comment("Translatable messages used across the entire plugin. MiniMessage is the only supported text format.")
    Messages messages();

    @ConfigSpec
    @NonExtendable
    interface Messages {

        // Command Errors

        @Order(0) @Key("error.missing_permission")
        @Comment("Command Errors")
        default String errorMissingPermission() {
            return "<dark_gray>› <red>Insufficient permissions to run this command.";
        }

        @Order(1) @Key("error.sender_not_player")
        default String errorSenderNotPlayer() {
            return "<dark_gray>› <red>This command can only be executed by a player.";
        }

        @Order(2) @Key("error.enum_not_found.display_type")
        default String errorEnumNotFoundDisplayType() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a valid display type.";
        }

        @Order(3) @Key("error.invalid_boolean")
        default String errorInvalidBoolean() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>must be either <yellow>true <red>or <yellow>false<red>.";
        }

        @Order(4) @Key("error.invalid_integer")
        default String errorInvalidInteger() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a valid integer.";
        }

        @Order(5) @Key("error.invalid_decimal")
        default String errorInvalidDecimal() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a valid number.";
        }

        @Order(6) @Key("error.invalid_registry_value.item_type")
        default String errorInvalidRegistryValueItemType() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a valid item type.";
        }

        @Order(7) @Key("error.invalid_registry_value.block_type")
        default String errorInvalidRegistryValueBlockType() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a valid block type.";
        }

        @Order(8) @Key("error.invalid_command")
        default String errorInvalidCommand() {
            return "<dark_gray>› <red>Unknown command. Use <yellow>/display help<red> to view help page.";
        }

        @Order(8) @Key("error.invalid_display")
        default String errorInvalidDisplay() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is non-existent or non-reachable display.";
        }

        // Display > Help

        @Order(9) @Key("command.display.help")
        @Comment("Display > Help")
        default List<String> commandDisplayHelp() {
            return List.of("TO-DO");
        }

        // Display > Create

        @Order(10) @Key("command.display.create.usage")
        @Comment("Display > Create")
        default String commandDisplayCreateUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display create <secondary>(type) (name)";
        }

        @Order(11) @Key("command.display.create.success")
        default String commandDisplayCreateSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name}<gray> has been created.";
        }

        @Order(12) @Key("command.display.create.failure.invalid_format")
        default String commandDisplayCreateFailureInvalidFormat() {
            return "<dark_gray>› <red>Specified name does not match format: [<yellow>0-9 A-Z / . - _<red>]";
        }

        // Display > Delete

        @Order(13) @Key("command.display.delete.usage")
        @Comment("Display > Delete")
        default String commandDisplayDeleteUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display delete <secondary>(name)";
        }

        @Order(14) @Key("command.display.delete.success")
        default String commandDisplayDeleteSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name}<gray> has been deleted.";
        }

        // Display > Respawn

        @Order(15) @Key("command.display.respawn.usage")
        @Comment("Display > Edit > Respawn")
        default String commandDisplayRespawnUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display respawn <secondary>(display)";
        }

        @Order(16) @Key("command.display.respawn.success")
        default String commandDisplayRespawnSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name} <gray>has been respawned.";
        }

        // Display > Reload

        @Order(17) @Key("command.display.reload")
        @Comment("Display > Reload")
        default String commandDisplayReload() {
            return "<dark_gray>› <gray>Plugin <primary>DisplayEntities<gray> has been reloaded.";
        }

        // Display > Edit

        @Order(18) @Key("command.display.edit.usage")
        @Comment("Display > Edit")
        default String commandDisplayEditUsage() {
            return "<dark_gray>› <red>Missing arguments. Use <yellow>/display help<red> to view help page.";
        }

        // Display > Edit > Alignment

        @Order(18) @Key("command.display.edit.alignment.usage")
        @Comment("Display > Edit > Alignment")
        default String commandDisplayEditAlignmentUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>alignment <secondary>(alignment)";
        }

        @Order(19) @Key("command.display.edit.alignment.success")
        default String commandDisplayEditAlignmentSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{alignment} <gray>alignment.";
        }

        // Display > Edit > Background

        @Order(20) @Key("command.display.edit.background.usage")
        @Comment("Display > Edit > Background")
        default String commandDisplayEditBackgroundUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>background <secondary>(color) [transparency]";
        }

        @Order(21) @Key("command.display.edit.background.success")
        default String commandDisplayEditBackgroundSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{color} <gray>background color.";
        }

        // Display > Edit > Billboard

        @Order(22) @Key("command.display.edit.billboard.usage")
        @Comment("Display > Edit > Billboard")
        default String commandDisplayEditBillboardUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>billboard <secondary>(billboard)";
        }

        @Order(23) @Key("command.display.edit.billboard.success")
        default String commandDisplayEditBillboardSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{billboard} <gray>billboard.";
        }

        // Display > Edit > Scale

        @Order(24) @Key("command.display.edit.scale.usage")
        @Comment("Display > Edit > Scale")
        default String commandDisplayEditScaleUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>scale <secondary>(x) [y] [z]";
        }

        @Order(25) @Key("command.display.edit.scale.success")
        default String commandDisplayEditScaleSuccess() {
            return "<dark_gray>› <gray>Display scale has been set to <primary>{x}<gray>, <primary>{y}<gray>, <primary>{z}<gray>.";
        }

        // Display > Edit > View Range

        @Order(26) @Key("command.display.edit.view_range.usage")
        @Comment("Display > Edit > View Range")
        default String commandDisplayEditViewRangeUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>view_range <secondary>(range)";
        }

        @Order(27) @Key("command.display.edit.view_range.success")
        default String commandDisplayEditViewRangeSuccess() {
            return "<dark_gray>› <gray>Display view range has been set to <primary>{range}<gray>.";
        }

        // Display > Edit > Block

        @Order(28) @Key("command.display.edit.block.usage")
        @Comment("Display > Edit > Block")
        default String commandDisplayEditBlockUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>block <secondary>(block)";
        }

        @Order(29) @Key("command.display.edit.block.success")
        default String commandDisplayEditBlockSuccess() {
            return "<dark_gray>› <gray>Display block type has been set to <primary>{type}<gray>.";
        }

        @Order(30) @Key("command.display.edit.block.failure.specified_invalid_type")
        default String commandDisplayEditBlockFailureSpecifiedInvalidType() {
            return "<dark_gray>› <red>Selected item is not a valid block type.";
        }

        @Order(30) @Key("command.display.edit.block.failure.holding_invalid_type")
        default String commandDisplayEditBlockFailureHoldingInvalidType() {
            return "<dark_gray>› <red>Item currently held in your hand is not a valid block type.";
        }

        // Display > Edit > Item

        @Order(31) @Key("command.display.edit.item.usage")
        @Comment("Display > Edit > Item")
        default String commandDisplayEditItemUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>item <secondary>(type)";
        }

        @Order(32) @Key("command.display.edit.item.success")
        default String commandDisplayEditItemSuccess() {
            return "<dark_gray>› <gray>Display item type has been set to <primary>{type}<gray>.";
        }

        @Order(33) @Key("command.display.edit.item.failure.specified_invalid_type")
        default String commandDisplayEditItemFailureSpecifiedInvalidType() {
            return "<dark_gray>› <red>Specified item is not a valid item type.";
        }

        // Display > Edit > Add Line

        @Order(34) @Key("command.display.edit.add_line.usage")
        @Comment("Display > Edit > Add Line")
        default String commandDisplayEditAddLineUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>add_line <secondary>(text)";
        }

        @Order(35) @Key("command.display.edit.add_line.success")
        default String commandDisplayEditAddLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been added to the display.";
        }

        // Display > Edit > Remove Line

        @Order(36) @Key("command.display.edit.remove_line.usage")
        @Comment("Display > Edit > Remove Line")
        default String commandDisplayEditRemoveLineUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>remove_line <secondary>(line)";
        }

        @Order(37) @Key("command.display.edit.remove_line.failure.out_of_bounds")
        default String commandDisplayEditRemoveLineFailureOutOfBounds() {
            return "<dark_gray>› <red>Line <yellow>{number} <red>is out of bounds. (Max: {max})";
        }

        @Order(38) @Key("command.display.edit.remove_line.success")
        default String commandDisplayEditRemoveLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been removed from the display.";
        }

        // Display > Edit > Set Line

        @Order(39) @Key("command.display.edit.set_line.usage")
        @Comment("Display > Edit > Set Line")
        default String commandDisplayEditSetLineUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>set_line <secondary>(line) (text)";
        }

        @Order(40) @Key("command.display.edit.set_line.success")
        default String commandDisplayEditSetLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been modified.";
        }

        @Order(41) @Key("command.display.edit.set_line.failure.out_of_bounds")
        default String commandDisplayEditSetLineFailureOutOfBounds() {
            return "<dark_gray>› <red>Line <yellow>{number} <red>is out of bounds. (Max: {max})";
        }

        // Display > Edit > See Through

        @Order(42) @Key("command.display.edit.see_through.success")
        @Comment("Display > Edit > See Through")
        default String commandDisplayEditSeeThroughSuccess() {
            return "<dark_gray>› <gray>Display see through has been set to <primary>{state}<gray>.";
        }

        // Display > Edit > Text Shadow

        @Order(43) @Key("command.display.edit.text_shadow.success")
        @Comment("Display > Edit > Text Shadow")
        default String commandDisplayEditTextShadowSuccess() {
            return "<dark_gray>› <gray>Display text shadow has been set to <primary>{state}<gray>.";
        }

        // Display > Edit > Text Opacity

        @Order(44) @Key("command.display.edit.text_opacity.usage")
        @Comment("Display > Edit > Text Opacity")
        default String commandDisplayEditTextOpacityUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>text_opacity <secondary>(opacity)";
        }

        @Order(45) @Key("command.display.edit.text_opacity.success")
        default String commandDisplayEditTextOpacitySuccess() {
            return "<dark_gray>› <gray>Display text opacity has been set to <primary>{opacity}<gray>.";
        }

        // Display > Edit > Move To

        @Order(46) @Key("command.display.edit.move_to.usage")
        @Comment("Display > Edit > Move To")
        default String commandDisplayEditMoveToUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>move_to <secondary>(x) (y) (z)";
        }

        @Order(47) @Key("command.display.edit.move_to.success")
        default String commandDisplayEditMoveToSuccess() {
            return "<dark_gray>› <gray>Display has been moved to <primary>{x}<gray>, <primary>{y}<gray>, <primary>{z}<gray>.";
        }

        // Display > Edit > Brightness

        @Order(48) @Key("command.display.edit.brightness.usage")
        @Comment("Display > Edit > Brightness")
        default String commandDisplayEditBrightnessUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>brightness <secondary>(block | sky) (brightness)";
        }

        @Order(49) @Key("command.display.edit.brightness.success")
        default String commandDisplayEditBrightnessSuccess() {
            return "<dark_gray>› <gray>Display brightness has been modified. (B: <primary>{brightness_block}<gray>, S: <primary>{brightness_sky}<gray>)";
        }

        // Display > Edit > Line Width

        @Order(50) @Key("command.display.edit.line_width.usage")
        @Comment("Display > Edit > Line Width")
        default String commandDisplayEditLineWidthUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>line_width <secondary>(width)";
        }

        @Order(51) @Key("command.display.edit.line_width.success")
        default String commandDisplayEditLineWidthSuccess() {
            return "<dark_gray>› <gray>Display line width has been set to <primary>{width}<gray>.";
        }

        // Display > Edit > Refresh Interval

        @Order(52) @Key("command.display.edit.refresh_interval.usage")
        @Comment("Display > Edit > Refresh Interval")
        default String commandDisplayEditRefreshIntervalUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>refresh_interval <secondary>(ticks)";
        }

        @Order(53) @Key("command.display.edit.refresh_interval.success")
        default String commandDisplayEditRefreshIntervalSuccess() {
            return "<dark_gray>› <gray>Display refresh interval has been set to <primary>{ticks}<gray> ticks. It's not an immediate operation and may require respawning the entity.";
        }

        @Order(54) @Key("command.display.edit.refresh_interval.failure")
        default String commandDisplayEditRefreshIntervalFailure() {
            return "<dark_gray>› <red>Refresh interval must be either <yellow>default<red> or a positive integer value.";
        }

    }

    /* IMPLEMENTED BY SPEC */

    /**
     * Resets this {@link PluginConfiguration} instance to default values.
     */
    @Reset
    void reset();

    /**
     * Saves configuration to the file.
     */
    @Save
    void save();

    /**
     * Loads configuration from the file.
     */
    @Reload
    default void reload() {
        // Rebuilding MiniMessage instance.
        DisplayEntities.instance().rebuildMiniMessage();
        // Updating debug mode. It's stored as a field value to prevent calling debugMode() through the proxy each time debug message is about to be sent.
        DisplayEntities.instance().isDebugEnabled(debugMode());
    }

}
