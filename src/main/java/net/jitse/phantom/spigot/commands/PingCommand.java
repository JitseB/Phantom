package net.jitse.phantom.spigot.commands;

import net.jitse.api.reflections.ClassGetter;
import net.jitse.phantom.spigot.logging.SpigotLogger;
import net.jitse.phantom.spigot.Phantom;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PingCommand implements CommandExecutor {

    private final Class craftPlayerClazz;
    private final Class entityPlayerClazz;

    private final Phantom plugin;
    private final boolean failed;

    public PingCommand(Phantom plugin) {
        this.plugin = plugin;

        this.craftPlayerClazz = ClassGetter.getOBCClass("entity.CraftPlayer");
        this.entityPlayerClazz = ClassGetter.getNMSClass("EntityPlayer");

        if (craftPlayerClazz == null || entityPlayerClazz == null) {
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.WARN, "Could not get CraftPlayer and/or EntityPlayer classes, disabling ping command.");
            this.failed = true;
        } else {
            this.failed = false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("phantom.commands.ping")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.NoPermission")
                    .replace("%permission%", "phantom.commands.ping")));
            return true;
        }

        if (failed) {
            sender.sendMessage(ChatColor.RED + "This command is temporarily disabled, please contact an administrator.");
            return true;
        }

        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.Usage")
                    .replace("%syntax%", "/ping <player>")));
            return true;
        }

        if (args.length > 0) {
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.Player.Offline")
                        .replace("%player_name%", args[0])));
                return true;
            }

            int ping = getPing(target);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.Ping.Other")
                    .replace("%player_name%", target.getName())
                    .replace("%ping%", ping + "ms")
                    .replace("%color%", getColor(ping).toString())));
            return true;
        }

        Player player = (Player) sender;
        int ping = getPing(player);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.Ping.Self")
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
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, "Could not get ping of " + player.getName() + ". Message: " + exception.getMessage());
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
