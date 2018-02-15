package net.jitse.phantom.spigot;

import net.jitse.phantom.spigot.utilities.files.Config;
import net.jitse.phantom.spigot.utilities.logging.LogLevel;
import net.jitse.phantom.spigot.utilities.logging.SpigotLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Phantom extends JavaPlugin {

    private final Config messagesConfig = new Config(this, "messages.yml", "messages.yml");

    private SpigotLogger spigotLogger;

    @Override
    public void onEnable() {
        setupConfigs();

        // Todo add config option for max log level.
        spigotLogger = new SpigotLogger("Phantom", LogLevel.DEBUG);

        Bukkit.broadcastMessage(ChatColor.BOLD + "Phantom was enabled.");
    }

    private void setupConfigs() {
        messagesConfig.createIfNotExists();
    }

    public SpigotLogger getSpigotLogger() {
        return spigotLogger;
    }

    public Config getMessagesConfig() {
        return messagesConfig;
    }
}
