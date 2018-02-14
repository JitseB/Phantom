package net.jitse.phantom.spigot.common.commands;

import net.jitse.phantom.spigot.Phantom;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PluginsCommand implements CommandExecutor {

    private final Phantom plugin;

    public PluginsCommand(Phantom plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("phantom.commands.plugins")) {
//            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.NoPermission")
//                    .replace("%permission%", "phantom.commands.plugins")));
            return true;
        }

        Plugin[] plugins = plugin.getServer().getPluginManager().getPlugins();
        StringBuilder builder = new StringBuilder(ChatColor.WHITE + "Plugins (" + plugins.length + "): ");

        List<String> all = new ArrayList<>();
        for (Plugin plugin : plugins) {
            all.add((plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + plugin.getName()
                    + ChatColor.GRAY + " [v" + plugin.getDescription().getVersion() + "]");
        }

        builder.append(StringUtils.join(all, ", "));

        sender.sendMessage(builder.toString());
        return true;
    }
}
