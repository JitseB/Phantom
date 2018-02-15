package net.jitse.phantom.spigot.modules.weather;

import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.modules.Module;
import net.jitse.phantom.spigot.modules.weather.listeners.WeatherChangeListener;
import net.jitse.phantom.spigot.utilities.logging.LogLevel;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Set;

public class WeatherModuleManager extends Module {

    public WeatherModuleManager(Phantom plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        lock();

        getPlugin().getServer().getPluginManager().registerEvents(new WeatherChangeListener(getPlugin()), getPlugin());
    }

    private void lock() {
        Set<String> keys = getPlugin().getSettingsConfig().getConfigurationSection("Modifiers.World.Weather").getKeys(false);
        for (String name : keys) {
            World world = getPlugin().getServer().getWorld(name);
            if (world == null) {
                getPlugin().getSpigotLogger().log(LogLevel.WARN, "Failed to lock " +
                        "weather for world " + name + ", is it loaded?");
                continue;
            }

            boolean change = getPlugin().getSettingsConfig().getBoolean("Modifiers.World.Weather." + name + ".Change");
            String config = getPlugin().getSettingsConfig().getString("Modifiers.World.Weather." + name + ".LockTo");
            WeatherType weather = Arrays.stream(WeatherType.values())
                    .filter(type -> type.toString().equalsIgnoreCase(config)).findFirst().orElse(null);

            if (weather == null) {
                getPlugin().getSpigotLogger().log(LogLevel.WARN, "Check the settings.yml file. " +
                        "The weather lock type for world " + name + " is incorrect.");
            } else {
                world.setGameRuleValue("doWeatherCycle", String.valueOf(change));
                if (!change) {
                    world.setStorm(weather == WeatherType.RAIN || weather == WeatherType.STORM);
                    world.setThundering(weather == WeatherType.STORM);
                    getPlugin().getSpigotLogger().log(LogLevel.DEBUG, "Locked weather for world " +
                            name + " to " + weather.toString() + ".");
                }
            }
        }
    }
}
