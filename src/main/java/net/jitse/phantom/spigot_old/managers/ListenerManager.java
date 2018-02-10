package net.jitse.phantom.spigot_old.managers;

import net.jitse.phantom.spigot_old.Phantom;
import net.jitse.phantom.spigot_old.listeners.account.AccountRankChangedListener;
import net.jitse.phantom.spigot_old.listeners.account.PlayerJoinedListener;
import net.jitse.phantom.spigot_old.listeners.general.CommandProcessListener;
import net.jitse.phantom.spigot_old.listeners.general.ServerListPingListener;
import net.jitse.phantom.spigot_old.listeners.player.AsyncPlayerPreLoginListener;
import net.jitse.phantom.spigot_old.listeners.player.ChatListener;
import net.jitse.phantom.spigot_old.listeners.player.PlayerJoinListener;
import net.jitse.phantom.spigot_old.listeners.player.PlayerMoveListener;
import net.jitse.phantom.spigot_old.listeners.world.WeatherChangeListener;
import net.jitse.phantom.spigot_old.logging.SpigotLogger;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ListenerManager {

    private final Phantom plugin;
    private final Set<Listener> listeners;

    public ListenerManager(Phantom plugin) {
        this.plugin = plugin;
        this.listeners = new HashSet<>();

        // Add listeners here:
        Collections.addAll(listeners,
                new AsyncPlayerPreLoginListener(plugin),
                new PlayerJoinListener(plugin),
                new WeatherChangeListener(plugin),
                new PlayerJoinedListener(plugin),
                new ChatListener(plugin),
                new AccountRankChangedListener(plugin),
                new CommandProcessListener(plugin),
                new ServerListPingListener(plugin),
                new PlayerMoveListener(plugin)
        );
    }

    public void registerAll() {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
        SpigotLogger.log(plugin, SpigotLogger.LogLevel.DEBUG, "Registered a total of " + listeners.size() + " event listeners.");
    }
}
