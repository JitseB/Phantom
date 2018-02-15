package net.jitse.phantom.spigot.modules.common;

import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.modules.Module;
import net.jitse.phantom.spigot.modules.common.commands.PingCommand;
import net.jitse.phantom.spigot.modules.common.commands.PluginsCommand;
import net.jitse.phantom.spigot.modules.common.listeners.PlayerJoinListener;

public class CommonManager extends Module {

    public CommonManager(Phantom plugin) {
        super(plugin);
    }

    @Override
    public void register() {
        getPlugin().getCommand("ping").setExecutor(new PingCommand(getPlugin()));
        getPlugin().getCommand("plugins").setExecutor(new PluginsCommand(getPlugin()));

        getPlugin().getServer().getPluginManager().registerEvents(new PlayerJoinListener(), getPlugin());
    }
}
