package net.jitse.phantom.spigot.modules;

import net.jitse.phantom.spigot.Phantom;

public class ModuleManager {

    private final Phantom plugin;

    public ModuleManager(Phantom plugin) {
        this.plugin = plugin;

        if (plugin.getModuleManager() != null) {
            throw new RuntimeException("Cannot create another ModuleManager instance.");
        }
    }

    public boolean loadAllModules() {
        return true;
    }
}
