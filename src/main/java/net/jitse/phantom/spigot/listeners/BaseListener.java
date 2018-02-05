package net.jitse.phantom.spigot.listeners;

import net.jitse.phantom.spigot.Phantom;
import org.bukkit.event.Listener;

public abstract class BaseListener implements Listener {

    private final Phantom plugin;

    public BaseListener(Phantom plugin) {
        this.plugin = plugin;
    }

    public Phantom getPlugin() {
        return plugin;
    }
}
