package net.jitse.phantom.spigot.utilities.listener;

import net.jitse.phantom.spigot.Phantom;
import org.bukkit.event.Listener;

public abstract class ListenerBase implements Listener {

    private final Phantom plugin;

    public ListenerBase(Phantom plugin) {
        this.plugin = plugin;
    }

    public Phantom getPlugin() {
        return plugin;
    }
}
