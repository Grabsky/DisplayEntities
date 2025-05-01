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

    }

    /**
     * Saves default configuration to the file.
     */
    @IgnoreMethod
    default void performSave() {
        // Saving the configuration.
        DisplayEntities.instance().configuration().save();
    }

    /**
     * Loads configuration from the file.
     */
    @IgnoreMethod
    default void performReload() {
        // Reloading the configuration.
        DisplayEntities.instance().configuration().reload();
        // Extra logic goes here...
        DisplayEntities.instance().rebuildMiniMessage();
    }

    /* IMPLEMENTED BY SPEC */

    /**
     * @apiNote This is for internal use only. Please use {@link #performSave() PluginConfiguration.performSave()} method instead.
     */
    @Save @Deprecated @SuppressWarnings("DeprecatedIsStillUsed")
    void save();

    /**
     * @apiNote This is for internal use only. Please use {@link #performReload() PluginConfiguration.performReload()} method instead.
     */
    @Reload @Deprecated @SuppressWarnings("DeprecatedIsStillUsed")
    void reload();

}
