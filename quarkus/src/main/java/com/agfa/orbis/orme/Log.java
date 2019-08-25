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

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Log {
    private LogLevel level;
    private String message;
    private LocalDateTime date;

    public static final DateTimeFormatter FORMATTER = ofPattern("yyyy-MM-dd HH:mm:ss:ms");

    public Log(LogLevel level, String message) {
        this.level = level;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    @JsonbTransient
    public LogLevel getLevel() {
        return level;
    }

    @JsonbTransient
    public String getMessage() {
        return message;
    }

    @JsonbTransient
    public LocalDateTime getDate() {
        return date;
    }

    @JsonbProperty("msg")
    public String message() {
        return FORMATTER.format(date) + " - " + level + " - " + message;
    }

    @Override
    public String toString() {
        return message();
    }
}
