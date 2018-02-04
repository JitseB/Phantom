package net.jitse.phantom.commands;

import net.jitse.phantom.Phantom;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PluginsCommand implements CommandExecutor {

    private final Phantom plugin;

    public PluginsCommand(Phantom plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("phantom.commands.plugins")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.NoPermission")
                    .replace("%permission%", "phantom.commands.plugins")));
            return true;
        }

        Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
        StringBuilder builder = new StringBuilder(ChatColor.WHITE + "Plugins (" + plugins.length + "): ");

        for (Plugin plugin : plugins) {
            builder.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED).append(plugin.getName())
                    .append(ChatColor.GRAY).append(" [v").append(plugin.getDescription().getVersion()).append("] ");
        }

        sender.sendMessage(builder.toString());
        return true;
    }
}
