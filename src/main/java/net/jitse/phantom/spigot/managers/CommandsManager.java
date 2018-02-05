package net.jitse.phantom.spigot.managers;

import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.commands.PingCommand;
import net.jitse.phantom.spigot.commands.PluginsCommand;
import net.jitse.phantom.spigot.commands.RankCommand;
import net.jitse.phantom.spigot.commands.VersionCommand;

public class CommandsManager {

    private final Phantom plugin;

    public CommandsManager(Phantom plugin) {
        this.plugin = plugin;
    }

    public void registerAll() {
        plugin.getCommand("rank").setExecutor(new RankCommand(plugin));
        plugin.getCommand("plugins").setExecutor(new PluginsCommand(plugin));
        plugin.getCommand("ping").setExecutor(new PingCommand(plugin));
        plugin.getCommand("version").setExecutor(new VersionCommand(plugin));
    }
}
