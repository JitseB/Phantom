package net.jitse.phantom.spigot_old.listeners.world;

import net.jitse.phantom.spigot_old.Phantom;
import net.jitse.phantom.spigot_old.listeners.BaseListener;
import net.jitse.phantom.spigot_old.logging.SpigotLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Arrays;

public class WeatherChangeListener extends BaseListener {

    public WeatherChangeListener(Phantom plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!getPlugin().getSettingsConfig().contains("Modifiers.World.Weather." + event.getWorld().getName())) {
            return;
        }

        boolean allowed = getPlugin().getSettingsConfig().getBoolean("Modifiers.World.Weather." + event.getWorld().getName() + ".Change");
        if (!allowed) {
            String config = getPlugin().getSettingsConfig().getString("Modifiers.World.Weather." + event.getWorld().getName() + ".LockTo");
            WeatherType weather = Arrays.stream(WeatherType.values()).filter(type -> type.toString().equalsIgnoreCase(config)).findFirst().orElse(null);
            if (weather == null) {
                SpigotLogger.log(getPlugin(), SpigotLogger.LogLevel.WARN, "Check the settings.yml file. The weather lock type is incorrect.");
            } else {
                // Event#toWeatherState returns true if it's being changed to raining.
                if (event.toWeatherState()) {
                    event.setCancelled(weather == WeatherType.SUN);
                }
                event.getWorld().setStorm(weather == WeatherType.RAIN || weather == WeatherType.STORM);
                event.getWorld().setThundering(weather == WeatherType.STORM);
            }
        }
    }

    public enum WeatherType {SUN, RAIN, STORM}
}
