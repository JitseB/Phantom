package net.jitse.phantom.spigot.managers;

import net.jitse.api.logging.Logger;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.listeners.account.AccountRankChangedListener;
import net.jitse.phantom.spigot.listeners.account.PlayerJoinedListener;
import net.jitse.phantom.spigot.listeners.general.CommandProcessListener;
import net.jitse.phantom.spigot.listeners.general.ServerListPingListener;
import net.jitse.phantom.spigot.listeners.player.AsyncPlayerPreLoginListener;
import net.jitse.phantom.spigot.listeners.player.ChatListener;
import net.jitse.phantom.spigot.listeners.player.PlayerJoinListener;
import net.jitse.phantom.spigot.listeners.world.WeatherChangeListener;
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
                new ServerListPingListener(plugin)
        );
    }

    public void registerAll() {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
        Logger.log(plugin, Logger.LogLevel.DEBUG, "Registered a total of " + listeners.size() + " event listeners.");
    }
}
