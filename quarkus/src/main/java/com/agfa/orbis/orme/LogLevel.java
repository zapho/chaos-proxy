/*
 *                  C O P Y R I G H T  (c) 2014
 *     A G F A   H E A L T H C A R E   C O R P O R A T I O N
 *                     All Rights Reserved
 *
 *
 *         THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
 *                       AGFA CORPORATION
 *        The copyright notice above does not evidence any
 *       actual or intended publication of such source code.
 */
package com.agfa.orbis.orme;

import java.util.Optional;

public enum LogLevel {
    TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4);

    private final int rank;

    LogLevel(int rank) {
        this.rank = rank;
    }

    public static Optional<LogLevel> from(String name) {
        for (LogLevel value : LogLevel.values()) {
            if (value.name().equalsIgnoreCase(name)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }

    public int getRank() {
        return rank;
    }
}
