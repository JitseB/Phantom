package net.jitse.phantom.spigot_old;

import com.google.gson.Gson;
import net.jitse.api.configuration.Config;
import net.jitse.api.storage.AccountStorage;
import net.jitse.api.storage.AuthStorage;
import net.jitse.api.storage.StorageType;
import net.jitse.phantom.spigot_old.file.TextFiles;
import net.jitse.phantom.spigot_old.logging.SpigotLogger;
import net.jitse.phantom.spigot_old.managers.*;
import net.jitse.phantom.spigot_old.storage.redis.RedisStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class Phantom extends JavaPlugin {

    private final Gson gson = new Gson();
    private final RanksManager ranksManager = new RanksManager(this);
    private final AccountManager accountManager = new AccountManager(this);
    private final AuthManager authManager = new AuthManager();

    private AuthStorage storage;
    private AccountStorage redis;
    private Config settingsConfig, messagesConfig, permissionsConfig, storageConfig, ranksConfig;
    
    @Override
    public void onEnable() {
        // Load configuration files.
        loadConfigs();

        // Create thank you and copyright files.
        new TextFiles(this).create();

        // Load, create and test storage systems.
        if (!loadStorage() || !loadRedis()) {
            return;
        }

        // Register all listeners and commands.
        new ListenerManager(this).registerAll();
        new CommandsManager(this).registerAll();

        // Create all world managers.
        createWorldManagers();

        // Load in the ranks.
        if (!loadRanks()) {
            return;
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

    private void loadConfigs() {
        this.settingsConfig = new Config(this, "settings.yml", "settings.yml");
        this.messagesConfig = new Config(this, "messages.yml", "messages.yml");
        this.permissionsConfig = new Config(this, "permissions.yml", "permissions.yml");
        this.storageConfig = new Config(this, "storage.yml", "storage.yml");
        this.ranksConfig = new Config(this, "ranks.yml", "ranks.yml");
    }

    private boolean loadStorage() {
        StorageType storageType = StorageType.get(storageConfig.getString("Type")).orElse(null);
        this.storage = (storageType == null ? null : storageType.getStorage(this, storageConfig));
        if (storage == null) {
            SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Invalid storage type found in storage.yml.");
            SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Could not boot plugin, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }

        if (!storage.createStorage()) {
            SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Could not boot plugin, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return false;
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
        return true;
    }

    private boolean loadRedis() {
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
                return false;
            }
            getServer().getScheduler().runTaskAsynchronously(this, () -> {
                if (!redis.testStorage()) {
                    SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Redis pool failure, disabling plugin...");
                    getServer().getPluginManager().disablePlugin(this);
                }
            });
        }
        return true;
    }

    private void createWorldManagers() {
        new WorldTimeManager(this).lock();
        new WorldWeatherManager(this).lock();
    }

    private boolean loadRanks() {
        if (!ranksManager.load(ranksConfig)) {
            SpigotLogger.log(this, SpigotLogger.LogLevel.FATAL, "Could not boot plugin, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    public Gson getGson() {
        return gson;
    }

    public AuthStorage getStorage() {
        return storage;
    }

    public boolean useRedis() {
        return redis != null;
    }

    public AccountStorage getRedis() {
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
