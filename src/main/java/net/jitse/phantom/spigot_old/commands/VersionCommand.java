package net.jitse.phantom.spigot_old.commands;

import net.jitse.phantom.spigot_old.Phantom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VersionCommand implements CommandExecutor {

    private final Phantom plugin;

    public VersionCommand(Phantom plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "This server is running CraftBukkit version " +
                Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ") " +
                "using Phantom version " + plugin.getDescription().getVersion() + ".");
        return true;
    }
}
