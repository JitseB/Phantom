package net.jitse.phantom.spigot;

import com.google.gson.Gson;
import net.jitse.api.configuration.Config;
import net.jitse.api.storage.Storage;
import net.jitse.api.storage.StorageType;
import net.jitse.phantom.spigot.file.TextFiles;
import net.jitse.phantom.spigot.logging.SpigotLogger;
import net.jitse.phantom.spigot.managers.*;
import net.jitse.phantom.spigot.storage.redis.RedisStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class Phantom extends JavaPlugin {

    private Gson gson;
    private Storage storage, redis;
    private Config settingsConfig, messagesConfig, permissionsConfig;
    private RanksManager ranksManager;
    private AccountManager accountManager;
    private AuthManager authManager;

    @Override
    public void onEnable() {
        this.gson = new Gson();

        // Load configuration files.
        this.settingsConfig = new Config(this, "settings.yml", "settings.yml");
        this.messagesConfig = new Config(this, "messages.yml", "messages.yml");
        this.permissionsConfig = new Config(this, "permissions.yml", "permissions.yml");
        Config storageConfig = new Config(this, "storage.yml", "storage.yml");
        Config ranksConfig = new Config(this, "ranks.yml", "ranks.yml");

        // Create thank you and copyright files.
        new TextFiles(this).create();

        // Load storage class.
        StorageType storageType = StorageType.get(storageConfig.getString("Type")).orElse(null);
        this.storage = (storageType == null ? null : storageType.getStorage(this, storageConfig));
        if (storage == null) {
            SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Invalid storage type found in storage.yml.");
            SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Could not boot plugin, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register event listeners and command executors.
        new ListenerManager(this).registerAll();
        new CommandsManager(this).registerAll();

        // World management (time and weather locking).
        new WorldTimeManager(this).lock();
        new WorldWeatherManager(this).lock();

        // Load in the ranks.
        this.ranksManager = new RanksManager(this);
        if (!ranksManager.load(ranksConfig)) {
            SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Could not boot plugin, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.accountManager = new AccountManager(this);
        this.authManager = new AuthManager();

        // Create and test storage system.
        SpigotLogger.log(this, SpigotLogger.LogLevel.DEBUG, "Creating storage for type " + storageType.toString() + ".");
        if (!storage.createStorage()) {
            SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Could not boot plugin, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            if (!storage.testStorage()) {
                SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Storage failure, disabling plugin...");
                getServer().getPluginManager().disablePlugin(this);
            }

            if (!storage.createPrerequisites()) {
                SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Storage failure, disabling plugin...");
                getServer().getPluginManager().disablePlugin(this);
            }

            accountManager.loadAll();
        });

        // Redis connection pool.
        if (storageConfig.getBoolean("Redis.Enabled")) {
            String host = storageConfig.getString("Redis.Login.Host");
            int port = storageConfig.getInt("Redis.Login.Port");
            String password = storageConfig.getString("Redis.Login.Password");
            boolean ssl = storageConfig.getBoolean("Redis.Login.SSL");

            this.redis = new RedisStorage(this, host, port, password, ssl);

            // Create and test Redis system.
            if (!redis.createStorage()) {
                SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Could not boot plugin, disabling...");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            getServer().getScheduler().runTaskAsynchronously(this, () -> {
                if (!redis.testStorage()) {
                    SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Redis pool failure, disabling plugin...");
                    getServer().getPluginManager().disablePlugin(this);
                }
            });
        }

        SpigotLogger.log(this, SpigotLogger.LogLevel.SUCCESS, "Enabled plugin.");
    }

    @Override
    public void onDisable() {
        if (storage.isOperational()) {
            storage.stopStorage();
        }

        if (useRedis() && redis.isOperational()) {
            redis.stopStorage();
        }

        SpigotLogger.log(this, SpigotLogger.LogLevel.SUCCESS, "Disabled plugin.");
    }

    public Gson getGson() {
        return gson;
    }

    public Storage getStorage() {
        return storage;
    }

    public boolean useRedis() {
        return redis != null;
    }

    public Storage getRedis() {
        return redis;
    }

    public Config getSettingsConfig() {
        return settingsConfig;
    }

    public Config getMessagesConfig() {
        return messagesConfig;
    }

    public Config getPermissionsConfig() {
        return permissionsConfig;
    }

    public RanksManager getRanksManager() {
        return ranksManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}
