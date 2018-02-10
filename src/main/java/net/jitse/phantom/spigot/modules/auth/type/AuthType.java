package net.jitse.phantom.spigot.modules.auth.type;

import java.util.Arrays;
import java.util.Optional;

public enum AuthType {

    NONE, PIN, PHRASE;

    public static Optional<AuthType> byString(String name) {
        return Arrays.stream(values()).filter(type -> type.toString().equalsIgnoreCase(name)).findFirst();
    }
}
