package net.jitse.phantom.spigot.managers;

import net.jitse.api.logging.Logger;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.listeners.world.WeatherChangeListener;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Set;

public class WorldWeatherManager {

    private final Phantom plugin;

    public WorldWeatherManager(Phantom plugin) {
        this.plugin = plugin;
    }

    public void lock() {
        Set<String> keys = plugin.getSettingsConfig().getConfigurationSection("Modifiers.World.Weather").getKeys(false);
        for (String name : keys) {
            World world = plugin.getServer().getWorld(name);
            if (world == null) {
                Logger.log(plugin, Logger.LogLevel.WARN, "Failed to lock weather for world " + name + ", is it loaded?");
                continue;
            }

            boolean change = plugin.getSettingsConfig().getBoolean("Modifiers.World.Weather." + name + ".Change");
            String config = plugin.getSettingsConfig().getString("Modifiers.World.Weather." + name + ".LockTo");
            WeatherChangeListener.WeatherType weather = Arrays.stream(WeatherChangeListener.WeatherType.values())
                    .filter(type -> type.toString().equalsIgnoreCase(config)).findFirst().orElse(null);

            if (weather == null) {
                Logger.log(plugin, Logger.LogLevel.WARN, "Check the settings.yml file. The weather lock type for world " + name + " is incorrect.");
            } else {
                world.setGameRuleValue("doWeatherCycle", String.valueOf(change));
                if (!change) {
                    world.setStorm(weather == WeatherChangeListener.WeatherType.RAIN || weather == WeatherChangeListener.WeatherType.STORM);
                    world.setThundering(weather == WeatherChangeListener.WeatherType.STORM);
                    Logger.log(plugin, Logger.LogLevel.DEBUG, "Locked weather for world " + name + " to " + weather.toString() + ".");
                }
            }
        }
    }
}
