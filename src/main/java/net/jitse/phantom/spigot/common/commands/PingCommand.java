package net.jitse.phantom.spigot.common.commands;

import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.utilities.logging.LogLevel;
import net.jitse.phantom.spigot.utilities.reflections.ClassGetter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PingCommand implements CommandExecutor {

    private final Class craftPlayerClazz = ClassGetter.getOBCClass("entity.CraftPlayer");
    private final Class entityPlayerClazz = ClassGetter.getNMSClass("EntityPlayer");

    private final Phantom plugin;

    private boolean failed = false;

    public PingCommand(Phantom plugin) {
        this.plugin = plugin;

        if (craftPlayerClazz == null || entityPlayerClazz == null) {
            plugin.getSpigotLogger().log(LogLevel.WARN, "Could not get CraftPlayer and/or " +
                    "EntityPlayer classes, disabling ping command.");
            this.failed = true;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("phantom.commands.ping")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getMessagesConfig().getString("Command.NoPermission")
                            .replace("%permission%", "phantom.commands.ping")));
            return true;
        }

        if (failed) {
            sender.sendMessage(ChatColor.RED + "This command is temporarily disabled, please contact an administrator.");
            return true;
        }

        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getMessagesConfig().getString("Command.Usage")
                            .replace("%syntax%", "/ping <player>")));
            return true;
        }

        if (args.length > 0) {
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getMessagesConfig().getString("Command.Player.Offline")
                                .replace("%player_name%", args[0])));
                return true;
            }

            int ping = getPing(target);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getMessagesConfig().getString("Command.Ping.Other")
                            .replace("%player_name%", target.getName())
                            .replace("%ping%", ping + "ms")
                            .replace("%color%", getColor(ping).toString())));
            return true;
        }

        Player player = (Player) sender;
        int ping = getPing(player);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getMessagesConfig().getString("Command.Ping.Self")
                        .replace("%ping%", ping + "ms")
                        .replace("%color%", getColor(ping).toString())));
        return true;
    }

    private int getPing(Player player) {
        int ping = 0;
        try {
            Object craftPlayer = craftPlayerClazz.cast(player);
            Method getHandle = craftPlayer.getClass().getMethod("getHandle");
            Object entityPlayer = getHandle.invoke(craftPlayer);
            Field pingField = entityPlayerClazz.getField("ping");
            ping = (int) pingField.get(entityPlayer);
        } catch (Exception exception) {
            plugin.getSpigotLogger().log(LogLevel.ERROR, "Could not get ping of "
                    + player.getName() + ". Message: " + exception.getMessage());
        }
        return ping;
    }

    private ChatColor getColor(int ping) {
        if (ping < 150) {
            return ChatColor.DARK_GREEN;
        } else if (ping < 300) {
            return ChatColor.GREEN;
        } else if (ping < 600) {
            return ChatColor.YELLOW;
        } else if (ping < 1000) {
            return ChatColor.RED;
        } else {
            return ChatColor.DARK_RED;
        }
    }
}
