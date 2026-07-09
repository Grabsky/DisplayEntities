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

import java.util.UUID;

public final class Conditions {

    /**
     * Returns {@code true} if {@code num} is in range between {@code rangeFrom} and {@code rangeTo}.
     */
    public static boolean inRange(final int num, final int rangeFrom, final int rangeTo) {
        return num >= rangeFrom && num <= rangeTo;
    }

    /**
     * Returns {@code true} if {@code num} is in range between {@code rangeFrom} and {@code rangeTo}.
     */
    public static boolean inRange(final long num, final long rangeFrom, final long rangeTo) {
        return num >= rangeFrom && num <= rangeTo;
    }

    /**
     * Returns {@code true} if {@code value} is a valid {@link java.util.UUID}.
     */
    public static boolean isUUID(final String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
