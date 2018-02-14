package net.jitse.phantom.spigot.utilities.files;

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
    }

    /**
     * Create the configuration file.
     *
     * @return Whether the file was created successfully.
     * Auto returns true if file already exists.
     */
    public boolean createIfNotExists() {
        if (file.exists()) {
            return true;
        }

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
            return false;
        }

        return true;
    }

    /**
     * Reload the configuration file.
     *
     * @return Whether the file was reloaded successfully.
     */
    public boolean reload() {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            return false;
        }

        return true;
    }

    /**
     * Save the configuration file.
     *
     * @return Whether the file was saved successfully.
     */
    public boolean save() {
        try {
            options().indent(2);
            save(file);
        } catch (IOException exception) {
            return false;
        }

        return true;
    }
}
