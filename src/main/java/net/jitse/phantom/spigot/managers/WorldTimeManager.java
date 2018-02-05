package net.jitse.phantom.spigot.managers;

import net.jitse.api.logging.Logger;
import net.jitse.phantom.spigot.Phantom;
import org.bukkit.World;

import java.util.Set;

public class WorldTimeManager {

    private final Phantom plugin;

    public WorldTimeManager(Phantom plugin) {
        this.plugin = plugin;
    }

    public void lock() {
        Set<String> keys = plugin.getSettingsConfig().getConfigurationSection("Modifiers.World.Time").getKeys(false);
        for (String name : keys) {
            World world = plugin.getServer().getWorld(name);
            if (world == null) {
                Logger.log(plugin, Logger.LogLevel.WARN, "Failed to lock time for world " + name + ", is it loaded?");
                continue;
            }
            boolean change = plugin.getSettingsConfig().getBoolean("Modifiers.World.Time." + name + ".Change");
            world.setGameRuleValue("doDaylightCycle", String.valueOf(change));
            if (!change) {
                try {
                    int time = plugin.getSettingsConfig().getInt("Modifiers.World.Time." + name + ".LockTo");
                    world.setTime(time);
                    Logger.log(plugin, Logger.LogLevel.DEBUG, "Locked time for world " + name + " to " + time + ".");
                } catch (NumberFormatException exception) {
                    Logger.log(plugin, Logger.LogLevel.WARN, "Failed to lock time for world " + name + ", because" +
                            " the time could not be parsed.");
                }
            }
        }
    }
}
