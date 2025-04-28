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
package cloud.grabsky.displayentities.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;

public enum LombokExtensions {
    INSTANCE; // SINGLETON

    public static String repl(final String self, final CharSequence text, final Object replacement) {
        return self.replace(text, String.valueOf(replacement));
    }

    public static void setRichText(final TextDisplay display, final String text) {
        display.text(MiniMessage.miniMessage().deserialize(text));
    }

    public static Location withX(final Location location, final double x) {
        return location.clone().set(x, location.y(), location.z());
    }

    public static Location withY(final Location location, final double y) {
        return location.clone().set(location.x(), y, location.z());
    }

    public static Location withZ(final Location location, final double z) {
        return location.clone().set(location.x(), location.y(), z);
    }

    public static Location withPitch(final Location location, final float pitch) {
        final Location copied = location.clone();
        copied.setPitch(pitch);
        return copied;
    }

    public static Location withYaw(final Location location, final float yaw) {
        final Location copied = location.clone();
        copied.setYaw(yaw);
        return copied;
    }

}
