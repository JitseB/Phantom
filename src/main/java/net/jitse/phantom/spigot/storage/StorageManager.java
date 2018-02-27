package net.jitse.phantom.spigot.storage;

import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.storage.sql.HikariStorage;
import net.jitse.phantom.spigot.utilities.logging.LogLevel;

public class StorageManager {

    private final Phantom plugin;

    private AuthStorage sql;
    private AccountStorage redis;

    public StorageManager(Phantom plugin) {
        this.plugin = plugin;
    }

    public boolean setup() {
        return setupSqlStorage() && setupRedisStorage();
    }

    private boolean setupSqlStorage() {
        StorageType type = StorageType.byString(plugin.getStorageConfig().getString("Type")).orElse(null);

        // If the type is invalid, stop enabling the storage systems.
        if (type == null) {
            return false;
        }


        switch (type) {
            case HIKARICP:
                String host = plugin.getStorageConfig().getString("Login.Host");
                int port = plugin.getStorageConfig().getInt("Login.Port");
                String username = plugin.getStorageConfig().getString("Login.Username");
                String password = plugin.getStorageConfig().getString("Login.Password");
                String database = plugin.getStorageConfig().getString("Login.Database");
                boolean ssl = plugin.getStorageConfig().getBoolean("Login.SSL");
                this.sql = new HikariStorage(plugin, host, port, username, password, database, ssl);
                break;
            case SQLITE:
                plugin.getSpigotLogger().log(LogLevel.FATAL, "Sqlite storage is currently not supported!");
                return false;
        }

        return sql.enable();
    }

    private boolean setupRedisStorage() {
        if (!plugin.getStorageConfig().getBoolean("Redis.Enabled")) {
            // If Redis is disabled, don't set it up...
            return true;
        }

        String host = plugin.getStorageConfig().getString("Redis.Login.Host");
        int port = plugin.getStorageConfig().getInt("Redis.Login.Port");
        String password = plugin.getStorageConfig().getString("Redis.Login.Password");
        boolean ssl = plugin.getStorageConfig().getBoolean("Redis.Login.SSL");

        this.redis = new RedisStorage(plugin, host, port, password, ssl);

        return redis.enable();
    }

    public AuthStorage getSqlStorage() {
        return sql;
    }

    public boolean useRedis() {
        return redis != null;
    }

    public AccountStorage getRedisStorage() {
        return redis;
    }
}
