package net.jitse.api.storage;

import net.jitse.api.configuration.Config;
import net.jitse.phantom.Phantom;
import net.jitse.phantom.storage.database.HikariStorage;
import net.jitse.phantom.storage.database.MySqlStorage;
import net.jitse.phantom.storage.file.SqliteStorage;

import java.util.Arrays;
import java.util.Optional;

public enum StorageType {
    MYSQL, HIKARI, SQLITE;

    public static Optional<StorageType> get(String str) {
        return Arrays.stream(values()).filter(type -> type.toString().equalsIgnoreCase(str)).findFirst();
    }

    public Storage getStorage(Phantom plugin, Config config) {
        String host = config.getString("Login.Host");
        int port = config.getInt("Login.Port");
        String user = config.getString("Login.User");
        String password = config.getString("Login.Password");
        String database = config.getString("Login.Database");
        boolean ssl = config.getBoolean("Login.SSL");

        switch (this) {
            case MYSQL:
                return new MySqlStorage(plugin, host, port, user, password, database, ssl);
            case HIKARI:
                return new HikariStorage(plugin, host, port, user, password, database, ssl);
            case SQLITE:
                return new SqliteStorage();
        }

        return null;
    }
}
