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
import revxrsal.spec.annotation.IgnoreMethod;
import revxrsal.spec.annotation.Key;
import revxrsal.spec.annotation.Order;
import revxrsal.spec.annotation.Reload;
import revxrsal.spec.annotation.Save;

import java.util.LinkedHashMap;

@ConfigSpec
public interface PluginConfiguration {

    @Order(0) @Key("debug_mode")
    @Comment("Debug mode prints additional information to the console. (Default: false)")
    default boolean debugMode() {
        return false;
    }

    @Order(1) @Key("refresh_interval")
    @Comment("Placeholders refresh interval in ticks. (Default: 100)")
    default int refreshInterval() {
        return 100;
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
    interface Messages {

        // Display > Create

        @Order(0) @Key("command.display.create.usage")
        @Comment("Display > Create")
        default String commandDisplayCreateUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display create (type) (name)";
        }

        @Order(1) @Key("command.display.create.success")
        default String commandDisplayCreateSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name}<gray> has been created.";
        }

        @Order(2) @Key("command.display.create.failure.invalid_format")
        default String commandDisplayCreateFailureInvalidFormat() {
            return "<dark_gray>› <red>Specified name does not match format: [<yellow>0-9 A-Z / . - _<red>]";
        }

        // Display > Delete

        @Order(3) @Key("command.display.delete.usage")
        @Comment("Display > Delete")
        default String commandDisplayDeleteUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display delete (name)";
        }

        @Order(4) @Key("command.display.delete.success")
        default String commandDisplayDeleteSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name}<gray> has been deleted.";
        }

        // Display > Reload

        @Order(5) @Key("command.display.reload")
        @Comment("Display > Reload")
        default String commandDisplayReload() {
            return "<dark_gray>› <gray>Plugin <primary>DisplayEntities<gray> has been reloaded.";
        }

        // Display > Edit > Alignment

        @Order(6) @Key("command.display.edit.alignment.usage")
        @Comment("Display > Edit > Alignment")
        default String commandDisplayEditAlignmentUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>alignment <secondary>(alignment)";
        }

        @Order(7) @Key("command.display.edit.alignment.success")
        default String commandDisplayEditAlignmentSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{alignment} <gray>alignment.";
        }

        // Display > Edit > Background

        @Order(8) @Key("command.display.edit.background.usage")
        @Comment("Display > Edit > Background")
        default String commandDisplayEditBackgroundUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>background <secondary>(color) [transparency]";
        }

        @Order(9) @Key("command.display.edit.background.success")
        default String commandDisplayEditBackgroundSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{color} <gray>background color.";
        }

        // Display > Edit > Billboard

        @Order(10) @Key("command.display.edit.billboard.usage")
        @Comment("Display > Edit > Billboard")
        default String commandDisplayEditBillboardUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>billboard <secondary>(billboard)";
        }

        @Order(11) @Key("command.display.edit.billboard.success")
        default String commandDisplayEditBillboardSuccess() {
            return "<dark_gray>› <gray>Display is now using <primary>{billboard} <gray>billboard.";
        }

        // Display > Edit > Scale

        @Order(12) @Key("command.display.edit.scale.usage")
        @Comment("Display > Edit > Scale")
        default String commandDisplayEditScaleUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>scale <secondary>(x) [y] [z]";
        }

        @Order(13) @Key("command.display.edit.scale.success")
        default String commandDisplayEditScaleSuccess() {
            return "<dark_gray>› <gray>Display scale has been set to <primary>{x}<gray>, <primary>{y}<gray>, <primary>{z}<gray>.";
        }

        // Display > Edit > View Range

        @Order(14) @Key("command.display.edit.view_range.usage")
        @Comment("Display > Edit > View Range")
        default String commandDisplayEditViewRangeUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>view_range <secondary>(range)";
        }

        @Order(15) @Key("command.display.edit.view_range.success")
        default String commandDisplayEditViewRangeSuccess() {
            return "<dark_gray>› <gray>Display view range has been set to <primary>{range}<gray>.";
        }

        // Display > Edit > Block

        @Order(16) @Key("command.display.edit.block.usage")
        @Comment("Display > Edit > Block")
        default String commandDisplayEditBlockUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>block <secondary>(type)";
        }

        @Order(17) @Key("command.display.edit.block.success")
        default String commandDisplayEditBlockSuccess() {
            return "<dark_gray>› <gray>Display block type has been set to <primary>{type}<gray>.";
        }

        @Order(18) @Key("command.display.edit.block.failure")
        default String commandDisplayEditBlockFailure() {
            return "<dark_gray>› <red>Failed to set block type. Specify argument or make sure you are holding an item in your hand.";
        }

        // Display > Edit > Item

        @Order(19) @Key("command.display.edit.item.usage")
        @Comment("Display > Edit > Item")
        default String commandDisplayEditItemUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>item <secondary>(type)";
        }

        @Order(20) @Key("command.display.edit.item.success")
        default String commandDisplayEditItemSuccess() {
            return "<dark_gray>› <gray>Display item type has been set to <primary>{type}<gray>.";
        }

        @Order(21) @Key("command.display.edit.item.failure")
        default String commandDisplayEditItemFailure() {
            return "<dark_gray>› <red>Failed to set item type. Specify argument or make sure you are holding an item in your hand.";
        }

        // Display > Edit > Add Line

        @Order(22) @Key("command.display.edit.add_line.usage")
        @Comment("Display > Edit > Add Line")
        default String commandDisplayEditAddLineUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>add_line <secondary>(text)";
        }

        @Order(23) @Key("command.display.edit.add_line.success")
        default String commandDisplayEditAddLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been added to the display.";
        }

        // Display > Edit > Remove Line

        @Order(24) @Key("command.display.edit.remove_line.usage")
        @Comment("Display > Edit > Remove Line")
        default String commandDisplayEditRemoveLineUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>remove_line <secondary>(line)";
        }

        @Order(25) @Key("command.display.edit.remove_line.failure.out_of_bounds")
        default String commandDisplayEditRemoveLineFailureOutOfBounds() {
            return "<dark_gray>› <red>Line <yellow>{number} <red>is out of bounds. (Max: {max})";
        }

        @Order(26) @Key("command.display.edit.remove_line.success")
        default String commandDisplayEditRemoveLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been removed from the display.";
        }

        // Display > Edit > Set Line

        @Order(27) @Key("command.display.edit.set_line.usage")
        @Comment("Display > Edit > Set Line")
        default String commandDisplayEditSetLineUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>set_line <secondary>(line) (text)";
        }

        @Order(28) @Key("command.display.edit.set_line.success")
        default String commandDisplayEditSetLineSuccess() {
            return "<dark_gray>› <gray>Line <primary>{number} <gray>has been modified.";
        }

        @Order(29) @Key("command.display.edit.set_line.failure.out_of_bounds")
        default String commandDisplayEditSetLineFailureOutOfBounds() {
            return "<dark_gray>› <red>Line <yellow>{number} <red>is out of bounds. (Max: {max})";
        }

        // Display > Edit > See Through

        @Order(30) @Key("command.display.edit.see_through.success")
        @Comment("Display > Edit > See Through")
        default String commandDisplayEditSeeThroughSuccess() {
            return "<dark_gray>› <gray>Display see through has been set to <primary>{state}<gray>.";
        }

        // Display > Edit > Text Shadow

        @Order(31) @Key("command.display.edit.text_shadow.success")
        @Comment("Display > Edit > Text Shadow")
        default String commandDisplayEditTextShadowSuccess() {
            return "<dark_gray>› <gray>Display text shadow has been set to <primary>{state}<gray>.";
        }

        // Display > Edit > Text Opacity

        @Order(32) @Key("command.display.edit.text_opacity.usage")
        @Comment("Display > Edit > Text Opacity")
        default String commandDisplayEditTextOpacityUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>text_opacity <secondary>(opacity)";
        }

        @Order(33) @Key("command.display.edit.text_opacity.success")
        default String commandDisplayEditTextOpacitySuccess() {
            return "<dark_gray>› <gray>Display text opacity has been set to <primary>{opacity}<gray>.";
        }

        // Display > Edit > Move To

        @Order(34) @Key("command.display.edit.move_to.usage")
        @Comment("Display > Edit > Move To")
        default String commandDisplayEditMoveToUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>move_to <secondary>(x) (y) (z)";
        }

        @Order(35) @Key("command.display.edit.move_to.success")
        default String commandDisplayEditMoveToSuccess() {
            return "<dark_gray>› <gray>Display has been moved to <primary>{x}<gray>, <primary>{y}<gray>, <primary>{z}<gray>.";
        }

        // Display > Edit > Brightness

        @Order(36) @Key("command.display.edit.brightness.usage")
        @Comment("Display > Edit > Brightness")
        default String commandDisplayEditBrightnessUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>brightness <secondary>(block | sky) (brightness)";
        }

        @Order(37) @Key("command.display.edit.brightness.success")
        default String commandDisplayEditBrightnessSuccess() {
            return "<dark_gray>› <gray>Display brightness has been modified. (B: <primary>{brightness_block}<gray>, S: <primary>{brightness_sky}<gray>)";
        }

        // Display > Edit > Line Width

        @Order(38) @Key("command.display.edit.line_width.usage")
        @Comment("Display > Edit > Line Width")
        default String commandDisplayEditLineWidthUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>line_width <secondary>(width)";
        }

        @Order(39) @Key("command.display.edit.line_width.success")
        default String commandDisplayEditLineWidthSuccess() {
            return "<dark_gray>› <gray>Display line width has been set to <primary>{width}<gray>.";
        }

        // Display > Edit > Refresh Interval

        @Order(40) @Key("command.display.edit.refresh_interval.usage")
        @Comment("Display > Edit > Line Width")
        default String commandDisplayEditRefreshIntervalUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display edit <secondary>(display) <primary>refresh_interval <secondary>(ticks)";
        }

        @Order(41) @Key("command.display.edit.refresh_interval.success")
        default String commandDisplayEditRefreshIntervalSuccess() {
            return "<dark_gray>› <gray>Display refresh interval has been set to <primary>{ticks}<gray> ticks. It's not an immediate operation and may require respawning the entity.";
        }

        @Order(41) @Key("command.display.edit.refresh_interval.failure")
        default String commandDisplayEditRefreshIntervalFailure() {
            return "<dark_gray>› <red>Refresh interval must be either <yellow>default<red> or a positive integer value.";
        }

        // Display > Respawn

        @Order(42) @Key("command.display.respawn.usage")
        @Comment("Display > Edit > Line Width")
        default String commandDisplayRespawnUsage() {
            return "<dark_gray>› <gray>Usage: <primary>/display respawn <secondary>(display)";
        }

        @Order(43) @Key("command.display.respawn.success")
        default String commandDisplayRespawnSuccess() {
            return "<dark_gray>› <gray>Display <primary>{name} <gray>has been respawned.";
        }

    }

    /* IMPLEMENTED BY SPEC */

    /**
     * Saves default configuration to the file.
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
