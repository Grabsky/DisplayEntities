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
