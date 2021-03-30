package fr.zapho.chaosproxy;

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
