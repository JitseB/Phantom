package net.jitse.phantom.spigot;

import net.jitse.phantom.spigot.account.AccountManager;
import net.jitse.phantom.spigot.modules.ModuleManager;
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

    // Managers.
    private final AccountManager accountManager = new AccountManager();
    private final ModuleManager moduleManager = new ModuleManager();

    // Logging.
    private SpigotLogger spigotLogger;

    @Override
    public void onEnable() {
        if (!setupConfigs(settingsConfig, messagesConfig)) {
            // If failed -> Stop enabling the plugin any further.
            Bukkit.broadcastMessage(ChatColor.RED + "Phantom failed to create all config files.");
            return;
        }

        // Todo add config option for max log level.
        spigotLogger = new SpigotLogger("Phantom", LogLevel.DEBUG);

        Bukkit.broadcastMessage(ChatColor.GREEN + "Phantom was enabled.");
    }

    private boolean setupConfigs(Config... configs) {
        for (Config config : configs) {
            // If failed -> Stop creating the rest of the configs.
            if (!config.createIfNotExists()) {
                return false;
            }
        }

        return true;
    }

    public SpigotLogger getSpigotLogger() {
        return spigotLogger;
    }

    public Config getSettingsConfig() {
        return settingsConfig;
    }

    public Config getMessagesConfig() {
        return messagesConfig;
    }
}
