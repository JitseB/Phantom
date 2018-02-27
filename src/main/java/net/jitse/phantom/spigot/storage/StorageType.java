package net.jitse.phantom.spigot.storage;

import java.util.Arrays;
import java.util.Optional;

public enum StorageType {

    HIKARICP, SQLITE;

    public static Optional<StorageType> byString(String name) {
        return Arrays.stream(values()).filter(type -> type.toString().equalsIgnoreCase(name)).findFirst();
    }
}
