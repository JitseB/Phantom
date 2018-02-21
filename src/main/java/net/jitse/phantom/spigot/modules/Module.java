package net.jitse.phantom.spigot.modules;

import net.jitse.phantom.spigot.Phantom;

public abstract class Module {

    private final Phantom plugin;

    public Module(Phantom plugin) {
        this.plugin = plugin;
    }

    public Phantom getPlugin() {
        return plugin;
    }

    /**
     * Load / Enable the module.
     *
     * @return Whether the module was loaded successfully.
     */
    public abstract boolean enable();
}
