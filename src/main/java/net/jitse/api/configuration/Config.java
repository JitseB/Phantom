package net.jitse.api.configuration;

import net.jitse.phantom.logging.SpigotLogger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Config extends YamlConfiguration {

    private final JavaPlugin plugin;
    private final File file;
    private final String defaults;

    public Config(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    public void reload() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

                load(file);
                if (defaults != null) {
                    InputStreamReader reader = new InputStreamReader(plugin.getResource(defaults));
                    FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                    setDefaults(defaultsConfig);
                    options().copyDefaults(true);

                    reader.close();
                    save();
                }
            } catch (IOException | InvalidConfigurationException exception) {
                SpigotLogger.log(plugin, SpigotLogger.LogLevel.WARN, "Could not create config file " + file.getName() + ".");
                SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, exception.getMessage());
            }
        }

        try {
            load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.WARN, "Could not load config file " + file.getName() + ".");
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, exception.getMessage());
        }
    }

    public void save() {
        try {
            options().indent(2);
            save(file);
        } catch (IOException exception) {
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.WARN, "Could not save config file " + file.getName() + ".");
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, exception.getMessage());
        }
    }
}
