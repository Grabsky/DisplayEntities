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
            put("common_primary", "#1AD5FF");
            put("common_secondary", "#80E8FF");
            put("text_primary", "#FFAE19");
            put("text_secondary", "#FFD280");
            put("block_primary", "#FF19A3");
            put("block_secondary", "#FF80CC");
            put("item_primary", "#FF441A");
            put("item_secondary", "#FF9680");
            put("interaction_primary", "#FFFFFF");
            put("interaction_secondary", "#999999");
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
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a display entity type.";
        }

        @Order(3) @Key("error.enum_not_found.billboard")
        default String errorEnumNotFoundBillboard() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a billboard.";
        }

        @Order(4) @Key("error.invalid_boolean")
        default String errorInvalidBoolean() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>must be either <yellow>true <red>or <yellow>false<red>.";
        }

        @Order(5) @Key("error.invalid_integer")
        default String errorInvalidInteger() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not an integer.";
        }

        @Order(6) @Key("error.invalid_decimal")
        default String errorInvalidDecimal() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a number.";
        }

        @Order(7) @Key("error.invalid_registry_value.item_type")
        default String errorInvalidRegistryValueItemType() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not an item type.";
        }

        @Order(8) @Key("error.invalid_registry_value.block_type")
        default String errorInvalidRegistryValueBlockType() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a block type.";
        }

        @Order(9) @Key("error.invalid_command")
        default String errorInvalidCommand() {
            return "<dark_gray>› <red>Unknown command. Use <yellow>/display help<red> to view help page.";
        }

        @Order(10) @Key("error.invalid_display")
        default String errorInvalidDisplay() {
            return "<dark_gray>› <red>Incomplete command or non-reachable display entity.";
        }
        
        @Order(11) @Key("error.invalid_position")
        default String errorInvalidPosition() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a position.";
        }

        @Order(12) @Key("error.invalid_color")
        default String errorInvalidColor() {
            return "<dark_gray>› <red>Argument <yellow>{input} <red>is not a color.";
        }

        @Order(13) @Key("command_usage_format")
        @Comment("Command Usage Format")
        default String commandUsageFormat() {
            return "<dark_gray>› <gray>Usage: {usage}";
        }

        @Order(14) @Key("command_usages")
        @Comment("Command Usages")
        default LinkedHashMap<String, String> commandUsages() {
            return new LinkedHashMap<>() {{
                put("display.help",                  "<primary>/display help <secondary>[page]");
                put("display.clone",                 "<primary>/display clone <secondary>(display) (name)");
                put("display.create",                "<primary>/display create <secondary>(type) (name)");
                put("display.delete",                "<primary>/display delete <secondary>(name)");
                put("display.respawn",               "<primary>/display respawn <secondary>(display)");
                put("display.teleport",              "<primary>/display teleport <secondary>(display)");
                put("display.edit.scale",            "<common_primary>/display edit <common_secondary>(display) <common_primary>scale <common_secondary>(x) (y) (z)");
                put("display.edit.view_range",       "<common_primary>/display edit <common_secondary>(display) <common_primary>view_range <common_secondary>(range)");
                put("display.edit.move_to",          "<common_primary>/display edit <common_secondary>(display) <common_primary>move_to <common_secondary>(x) (y) (z)");
                put("display.edit.billboard",        "<common_primary>/display edit <common_secondary>(display) <common_primary>billboard <common_secondary>(billboard)");
                put("display.edit.brightness.block", "<common_primary>/display edit <common_secondary>(display) <common_primary>brightness block <common_secondary>(brightness)");
                put("display.edit.brightness.sky",   "<common_primary>/display edit <common_secondary>(display) <common_primary>brightness sky <common_secondary>(brightness)");
                put("display.edit.rotate_x",         "<common_primary>/display edit <common_secondary>(display) <common_primary>rotate_x <common_secondary>(degrees)");
                put("display.edit.rotate_y",         "<common_primary>/display edit <common_secondary>(display) <common_primary>rotate_y <common_secondary>(degrees)");
                put("display.edit.add_line",         "<text_primary>/display edit <text_secondary>(display) <text_primary>add_line <text_secondary>(text)");
                put("display.edit.remove_line",      "<text_primary>/display edit <text_secondary>(display) <text_primary>remove_line <text_secondary>(line)");
                put("display.edit.set_line",         "<text_primary>/display edit <text_secondary>(display) <text_primary>set_line <text_secondary>(line) (text)");
                put("display.edit.insert_line",      "<text_primary>/display edit <text_secondary>(display) <text_primary>insert_line <text_secondary>(line) (text)");
                put("display.edit.refresh_interval", "<text_primary>/display edit <text_secondary>(display) <text_primary>refresh_interval <text_secondary>(ticks)");
                put("display.edit.alignment",        "<text_primary>/display edit <text_secondary>(display) <text_primary>alignment <text_secondary>(alignment)");
                put("display.edit.background",       "<text_primary>/display edit <text_secondary>(display) <text_primary>background <text_secondary>(color) [opacity]");
                put("display.edit.line_width",       "<text_primary>/display edit <text_secondary>(display) <text_primary>line_width <text_secondary>(width)");
                put("display.edit.see_through",      "<text_primary>/display edit <text_secondary>(display) <text_primary>see_through <text_secondary>(true | false)");
                put("display.edit.text_shadow",      "<text_primary>/display edit <text_secondary>(display) <text_primary>text_shadow <text_secondary>(true | false)");
                put("display.edit.text_opacity",     "<text_primary>/display edit <text_secondary>(display) <text_primary>text_opacity <text_secondary>(opacity)");
                put("display.edit.block",            "<block_primary>/display edit <block_secondary>(display) <block_primary>block <block_secondary>(hand | type)");
                put("display.edit.block.glow",       "<block_primary>/display edit <block_secondary>(display) <block_primary>glow <block_secondary>(@none | color)");
                put("display.edit.item",             "<item_primary>/display edit <item_secondary>(display) <item_primary>item <item_secondary>(hand | type)");
                put("display.edit.transform",        "<item_primary>/display edit <item_secondary>(display) <item_primary>transform <item_secondary>(transform)");
                put("display.edit.item.glow",        "<item_primary>/display edit <item_secondary>(display) <item_primary>glow <item_secondary>(@none | color)");
                put("display.edit.width",            "<interaction_primary>/display edit <interaction_secondary>(display) <interaction_primary>width <interaction_secondary>(width)");
                put("display.edit.height",           "<interaction_primary>/display edit <interaction_secondary>(display) <interaction_primary>height <interaction_secondary>(height)");
                put("display.edit.response",         "<interaction_primary>/display edit <interaction_secondary>(display) <interaction_primary>response <interaction_secondary>(true | false)");
                // Additional glow entry for in-game 'invalid-usage' message. The reason why it is needed is because the glow command only available for item and block displays.
                put("display.edit.glow",          "<common_primary>/display edit <common_secondary>(display) <common_primary>glow <common_secondary>(@none | color)");
            }};
        }
        
        // Display > Help

        @Order(15) @Key("command.display.help.header")
        @Comment("Display > Help")
        default List<String> commandDisplayHelpHeader() {
            return List.of("<dark_gray><st>                              </st>   <gray>Plugin Help   <dark_gray><st>                              </st>", "");
        }

        @Order(16) @Key("command.display.help.footer")
        default List<String> commandDisplayHelpFooter() {
            return List.of("", "<dark_gray><st>                              </st>    <gray>Page {page}/{max_page}    <dark_gray><st>                              </st>");
        }

        @Order(17) @Key("command.display.help.contents")
        default List<String> commandDisplayHelpContents() {
            return List.of(
                    "<dark_gray>› <primary>/display reload<dark_gray> - <gray>Shows list of available commands.",
                    "<dark_gray>› <spec:messages.command_usages.display.help><dark_gray> - <gray>Shows list of available commands.",
                    "<dark_gray>› <spec:messages.command_usages.display.clone><dark_gray> - <gray>Copies specified display.",
                    "<dark_gray>› <spec:messages.command_usages.display.create><dark_gray> - <gray>Creates a new display.",
                    "<dark_gray>› <spec:messages.command_usages.display.delete><dark_gray> - <gray>Deletes specified display.",
                    "<dark_gray>› <spec:messages.command_usages.display.respawn><dark_gray> - <gray>Respawns specified display.",
                    "<dark_gray>› <spec:messages.command_usages.display.teleport><dark_gray> - <gray>Teleports to specified display.",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.scale>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.view_range>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.move_to>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.billboard>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.brightness.block>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.brightness.sky>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.rotate_x>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.rotate_y>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.add_line>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.remove_line>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.set_line>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.insert_line>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.refresh_interval>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.alignment>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.background>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.line_width>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.see_through>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.text_shadow>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.text_opacity>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.block>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.block.glow>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.item>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.transform>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.item.glow>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.width>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.height>",
                    "<dark_gray>› <spec:messages.command_usages.display.edit.response>"
            );
        }

        // Display > Create

        @Order(18) @Key("command.display.create.success")
        @Comment("Display > Create")
        default String commandDisplayCreateSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name}<gray> has been created.";
        }

        @Order(19) @Key("command.display.create.failure.invalid_format")
        default String commandDisplayCreateFailureInvalidFormat() {
            return "<dark_gray>› <red>Specified name does not match format: [<yellow>0-9 A-Z / . - _<red>]";
        }

        // Display > Delete

        @Order(20) @Key("command.display.delete.success")
        @Comment("Display > Delete")
        default String commandDisplayDeleteSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name}<gray> has been deleted.";
        }

        // Display > Respawn

        @Order(21) @Key("command.display.respawn.success")
        @Comment("Display > Respawn")
        default String commandDisplayRespawnSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name} <gray>has been respawned.";
        }

        // Display > Clone

        @Order(22) @Key("command.display.clone.success")
        @Comment("Display > Clone")
        default String commandDisplayCloneSuccess() {
            return "<dark_gray>› <gray>Display <primary>{original_name}<gray> cloned as <primary>{copied_name}<gray>.";
        }

        @Order(23) @Key("command.display.clone.failure.invalid_format")
        default String commandDisplayCloneFailureInvalidFormat() {
            return "<dark_gray>› <red>Specified name does not match format: [<yellow>0-9 A-Z / . - _<red>]";
        }

        // Display > Reload

        @Order(24) @Key("command.display.reload")
        @Comment("Display > Reload")
        default String commandDisplayReload() {
            return "<dark_gray>› <gray>Plugin <primary>DisplayEntities<gray> has been reloaded.";
        }

        // Display > Teleport

        @Order(25) @Key("command.display.teleport.success")
        @Comment("Display > Teleport")
        default String commandDisplayTeleportSuccess() {
            return "<dark_gray>› <gray>You have been teleported to <primary>{display}<gray>.";
        }



        // Display > Edit > Alignment

        @Order(26) @Key("command.display.edit.alignment.success")
        @Comment("Display > Edit > Alignment")
        default String commandDisplayEditAlignmentSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{alignment} <gray>alignment.";
        }

        // Display > Edit > Background

        @Order(27) @Key("command.display.edit.background.success")
        @Comment("Display > Edit > Background")
        default String commandDisplayEditBackgroundSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{color} <gray>background color.";
        }

        // Display > Edit > Billboard

        @Order(28) @Key("command.display.edit.billboard.success")
        @Comment("Display > Edit > Billboard")
        default String commandDisplayEditBillboardSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{billboard} <gray>billboard.";
        }

        // Display > Edit > Scale

        @Order(29) @Key("command.display.edit.scale.success")
        @Comment("Display > Edit > Scale")
        default String commandDisplayEditScaleSuccess() {
            return "<dark_gray>› <gray>Display scale has been set to <primary>{x}<gray>, <primary>{y}<gray>, <primary>{z}<gray>.";
        }

        // Display > Edit > View Range

        @Order(30) @Key("command.display.edit.view_range.success")
        @Comment("Display > Edit > View Range")
        default String commandDisplayEditViewRangeSuccess() {
            return "<dark_gray>› <gray>Display view range has been set to <primary>{range}<gray>.";
        }

        // Display > Edit > Block

        @Order(31) @Key("command.display.edit.block.success")
        @Comment("Display > Edit > Block")
        default String commandDisplayEditBlockSuccess() {
            return "<dark_gray>› <gray>Display block type has been set to <primary>{type}<gray>.";
        }

        @Order(32) @Key("command.display.edit.block.failure.specified_invalid_type")
        default String commandDisplayEditBlockFailureSpecifiedInvalidType() {
            return "<dark_gray>› <red>Selected item is not a valid block type.";
        }

        @Order(33) @Key("command.display.edit.block.failure.holding_invalid_type")
        default String commandDisplayEditBlockFailureHoldingInvalidType() {
            return "<dark_gray>› <red>Item currently held in your hand is not a valid block type.";
        }

        // Display > Edit > Item

        @Order(34) @Key("command.display.edit.item.success")
        @Comment("Display > Edit > Item")
        default String commandDisplayEditItemSuccess() {
            return "<dark_gray>› <gray>Display item type has been set to <primary>{type}<gray>.";
        }

        @Order(35) @Key("command.display.edit.item.failure.specified_invalid_type")
        default String commandDisplayEditItemFailureSpecifiedInvalidType() {
            return "<dark_gray>› <red>Specified item is not a valid item type.";
        }

        // Display > Edit > Add Line

        @Order(36) @Key("command.display.edit.add_line.success")
        @Comment("Display > Edit > Add Line")
        default String commandDisplayEditAddLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been added to the display.";
        }

        // Display > Edit > Insert Line

        @Order(37) @Key("command.display.edit.insert_line.failure.out_of_bounds")
        default String commandDisplayEditInsertLineFailureOutOfBounds() {
            return "<dark_gray>› <red>Line <yellow>{number} <red>is out of bounds. (Max: {max})";
        }

        @Order(38) @Key("command.display.edit.insert_line.success")
        @Comment("Display > Edit > Insert Line")
        default String commandDisplayEditInsertLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been added to the display. Remaining entries were shifted downwards.";
        }

        // Display > Edit > Remove Line

        @Order(39) @Key("command.display.edit.remove_line.failure.out_of_bounds")
        @Comment("Display > Edit > Remove Line")
        default String commandDisplayEditRemoveLineFailureOutOfBounds() {
            return "<dark_gray>› <red>Line <yellow>{number} <red>is out of bounds. (Max: {max})";
        }

        @Order(40) @Key("command.display.edit.remove_line.success")
        default String commandDisplayEditRemoveLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been removed from the display.";
        }

        // Display > Edit > Set Line

        @Order(41) @Key("command.display.edit.set_line.success")
        @Comment("Display > Edit > Set Line")
        default String commandDisplayEditSetLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been modified.";
        }

        @Order(42) @Key("command.display.edit.set_line.failure.out_of_bounds")
        default String commandDisplayEditSetLineFailureOutOfBounds() {
            return "<dark_gray>› <red>Line <yellow>{number} <red>is out of bounds. (Max: {max})";
        }

        // Display > Edit > See Through

        @Order(43) @Key("command.display.edit.see_through.success")
        @Comment("Display > Edit > See Through")
        default String commandDisplayEditSeeThroughSuccess() {
            return "<dark_gray>› <gray>Display see through has been set to <primary>{state}<gray>.";
        }

        // Display > Edit > Text Shadow

        @Order(44) @Key("command.display.edit.text_shadow.success")
        @Comment("Display > Edit > Text Shadow")
        default String commandDisplayEditTextShadowSuccess() {
            return "<dark_gray>› <gray>Display text shadow has been set to <primary>{state}<gray>.";
        }

        // Display > Edit > Text Opacity

        @Order(45) @Key("command.display.edit.text_opacity.success")
        @Comment("Display > Edit > Text Opacity")
        default String commandDisplayEditTextOpacitySuccess() {
            return "<dark_gray>› <gray>Display text opacity has been set to <primary>{opacity}<gray>.";
        }

        // Display > Edit > Move To

        @Order(46) @Key("command.display.edit.move_to.success")
        @Comment("Display > Edit > Move To")
        default String commandDisplayEditMoveToSuccess() {
            return "<dark_gray>› <gray>Display has been moved to <primary>{x}<gray>, <primary>{y}<gray>, <primary>{z}<gray>.";
        }

        // Display > Edit > Brightness

        @Order(47) @Key("command.display.edit.brightness.success")
        @Comment("Display > Edit > Brightness")
        default String commandDisplayEditBrightnessSuccess() {
            return "<dark_gray>› <gray>Display brightness has been modified. (B: <primary>{brightness_block}<gray>, S: <primary>{brightness_sky}<gray>)";
        }

        // Display > Edit > Line Width

        @Order(48) @Key("command.display.edit.line_width.success")
        @Comment("Display > Edit > Line Width")
        default String commandDisplayEditLineWidthSuccess() {
            return "<dark_gray>› <gray>Display line width has been set to <primary>{width}<gray>.";
        }

        // Display > Edit > Refresh Interval

        @Order(49) @Key("command.display.edit.refresh_interval.success")
        @Comment("Display > Edit > Refresh Interval")
        default String commandDisplayEditRefreshIntervalSuccess() {
            return "<dark_gray>› <gray>Display refresh interval has been set to <primary>{ticks}<gray> ticks. It's not an immediate operation and may require respawning the entity.";
        }

        @Order(50) @Key("command.display.edit.refresh_interval.failure")
        default String commandDisplayEditRefreshIntervalFailure() {
            return "<dark_gray>› <red>Refresh interval must be either <yellow>default<red> or a positive integer value.";
        }

        // Display > Edit > Rotate X

        @Order(51) @Key("command.display.edit.rotate_x.success")
        @Comment("Display > Edit > Rotate X")
        default String commandDisplayEditRotateXSuccess() {
            return "<dark_gray>› <gray>Display has been rotated. (Pitch: <primary>{pitch}°<gray>)";
        }

        // Display > Edit > Rotate X

        @Order(52) @Key("command.display.edit.rotate_y.success")
        @Comment("Display > Edit > Rotate Y")
        default String commandDisplayEditRotateYSuccess() {
            return "<dark_gray>› <gray>Display has been rotated. (Yaw: <primary>{yaw}°<gray>)";
        }

        // Display > Edit > Width (Interaction)

        @Order(53) @Key("command.display.edit.width.success")
        @Comment("Display > Edit > Width")
        default String commandDisplayEditWidthSuccess() {
            return "<dark_gray>› <gray>Interaction width has been set to <primary>{width}<gray>.";
        }

        // Display > Edit > Height (Interaction)

        @Order(54) @Key("command.display.edit.height.success")
        @Comment("Display > Edit > Height")
        default String commandDisplayEditHeightSuccess() {
            return "<dark_gray>› <gray>Interaction height has been set to <primary>{height}<gray>.";
        }

        // Display > Edit > Response (Interaction)

        @Order(55) @Key("command.display.edit.response.success")
        @Comment("Display > Edit > Response")
        default String commandDisplayEditResponseSuccess() {
            return "<dark_gray>› <gray>Interaction response has been set to <primary>{state}<gray>.";
        }

        // Display > Edit > Transform (Item)

        @Order(56) @Key("command.display.edit.transform.success")
        @Comment("Display > Edit > Transform")
        default String commandDisplayEditTransformSuccess() {
            return "<dark_gray>› <gray>Display transform has been set to <primary>{transform}<gray>.";
        }

        // Display > Edit > Glow (Common)

        @Order(57) @Key("command.display.edit.glow.color_change.success")
        @Comment("Display > Edit > Glow")
        default String commandDisplayEditGlowColorChangeSuccess() {
            return "<dark_gray>› <gray>Display glow color has been set to <primary>{color}<gray>.";
        }

        @Order(58) @Key("command.display.edit.glow.disabled.success")
        default String commandDisplayEditGlowDisabledSuccess() {
            return "<dark_gray>› <gray>Display glow has been disabled.";
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
