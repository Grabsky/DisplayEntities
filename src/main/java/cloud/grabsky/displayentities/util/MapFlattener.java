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

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapFlattener {

    /**
     * Flattens a nested map structure into a single-level map.
     * Nested keys are combined using a dot notation to represent their hierarchy.
     */
    public static Map<String, Object> flatten(final @NotNull Map<String, Object> map) {
        final Map<String, Object> flatMap = new HashMap<>();
        flattenRecursive("", map, flatMap);
        return flatMap;
    }

    @SuppressWarnings("unchecked")
    private static void flattenRecursive(final @NotNull String prefix, final @NotNull Map<String, Object> map, final @NotNull Map<String, Object> flatMap) {
        map.forEach((key, value) -> {
            key = (prefix.isEmpty() == false) ? prefix + "." + key : key;
            // Forwarding to the flattener if the value is a map.
            if (value instanceof Map<?, ?>)
                flattenRecursive(key, (Map<String, Object>) value, flatMap);
            // Otherwise, putting the flattened value in the map.
            else flatMap.put(key, value);
        });
    }

}
