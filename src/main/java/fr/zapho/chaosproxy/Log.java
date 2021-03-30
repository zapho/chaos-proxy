package fr.zapho.chaosproxy;

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
