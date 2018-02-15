package net.jitse.phantom.spigot.utilities.logging;

import java.util.Arrays;
import java.util.Optional;

public enum LogLevel {

    DEBUG, ERROR, FATAL, INFO, WARN, SUCCESS;

    public Optional<LogLevel> getByString(String name) {
        return Arrays.stream(values()).filter(level -> level.toString().equalsIgnoreCase(name)).findFirst();
    }
}
