package net.jitse.phantom.spigot;

import net.jitse.phantom.spigot.account.AccountManager;
import net.jitse.phantom.spigot.account.rank.RankManager;
import net.jitse.phantom.spigot.modules.ModuleManager;
import net.jitse.phantom.spigot.storage.StorageManager;
import net.jitse.phantom.spigot.utilities.files.Config;
import net.jitse.phantom.spigot.utilities.logging.LogLevel;
import net.jitse.phantom.spigot.utilities.logging.SpigotLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Phantom extends JavaPlugin {

    // Plugin Configuration.
    private final Config settingsConfig = new Config(this, "settings.yml", "settings.yml");
    private final Config messagesConfig = new Config(this, "messages.yml", "messages.yml");
    private final Config storageConfig = new Config(this, "storage.yml", "storage.yml");

    // Managers.
    private final AccountManager accountManager = new AccountManager(this);
    private final StorageManager storageManager = new StorageManager(this);
    private final ModuleManager moduleManager = new ModuleManager(this);
    private final RankManager rankManager = new RankManager(this);

    // Logging.
    private SpigotLogger spigotLogger;

    @Override
    public void onEnable() {
        // Todo add config option for max log level (settings.yml).
        spigotLogger = new SpigotLogger("Phantom", ChatColor.WHITE, ChatColor.GRAY, LogLevel.DEBUG);

        // Load the configuration files.
        if (!setupConfigs(settingsConfig, messagesConfig)) {
            // If failed -> Stop enabling the plugin any further.
            spigotLogger.log(LogLevel.FATAL, "Failed to load all config files.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Set up all storage systems.
        if (!storageManager.setup()) {
            // If failed -> Stop enabling the plugin any further.
            spigotLogger.log(LogLevel.FATAL, "Failed to set up storage systems.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load all (internal) modules.
        if (!moduleManager.loadAllModules()) {
            // If failed -> Stop enabling the plugin any further.
            spigotLogger.log(LogLevel.FATAL, "Failed to load all internal modules.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Now everything is loaded and we can start creating all prerequisites for the storage systems.
        // Of course we're running this async as they mostly involve IO operations.
        // Todo create server join states (OPEN, MAINTENANCE, BOOTING) for a minor update.
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if (!storageManager.testSystems()) {
                // If failed -> Disable Phantom.
            }

            if (!storageManager.createPrerequisites()) {
                // If failed -> Disable Phantom.
            }
        });

        spigotLogger.log(LogLevel.SUCCESS, "Enabled plugin.");
    }

    @Override
    public void onDisable() {
        spigotLogger.log(LogLevel.SUCCESS, "Disabled plugin.");
    }

    /**
     * Sets up all configuration files.
     *
     * @param configs The configs that need to be set up.
     * @return Whether all configs were loaded in successfully.
     */
    private boolean setupConfigs(Config... configs) {
        for (Config config : configs) {
            // If failed -> Stop creating the rest of the configs.
            if (!config.createIfNotExists()) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return The internal logger for Phantom.
     */
    public SpigotLogger getSpigotLogger() {
        return spigotLogger;
    }

    /**
     * @return The main settings config.
     */
    public Config getSettingsConfig() {
        return settingsConfig;
    }

    /**
     * @return The messages config.
     */
    public Config getMessagesConfig() {
        return messagesConfig;
    }

    /**
     * @return The storage config.
     */
    public Config getStorageConfig() {
        return storageConfig;
    }

    /**
     * @return The account manager for Phantom.
     */
    public AccountManager getAccountManager() {
        return accountManager;
    }

    /**
     * @return The storage manager for Phantom.
     */
    public StorageManager getStorageManager() {
        return storageManager;
    }

    /**
     * @return The (internal) module manager for Phantom.
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }
}
