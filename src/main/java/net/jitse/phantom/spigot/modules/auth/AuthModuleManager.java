package net.jitse.phantom.spigot.modules.auth;

import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.modules.Module;

public class AuthModuleManager extends Module {

    public AuthModuleManager(Phantom plugin) {
        super(plugin);
    }

    @Override
    public boolean enable() {
        return true;
    }
}
