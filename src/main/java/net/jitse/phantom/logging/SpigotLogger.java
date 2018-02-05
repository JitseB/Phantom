package net.jitse.phantom.logging;

import net.jitse.phantom.spigot.Phantom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SpigotLogger {

    public static void log(Phantom plugin, LogLevel level, String message) {
        String text;
        switch (level) {
            case DEBUG:
                text = ChatColor.YELLOW + "[Debug] ";
                break;
            case ERROR:
                text = ChatColor.RED + "[Error] ";
                break;
            case FATAL:
                text = ChatColor.DARK_RED + "[Fatal] ";
                break;
            case INFO:
                text = ChatColor.AQUA + "[Info] ";
                break;
            case WARN:
                text = ChatColor.GOLD + "[Warn] ";
                break;
            case SUCCESS:
                text = ChatColor.GREEN + "[Success] ";
                break;
            default:
                text = "";
                break;
        }

        text += ChatColor.GRAY + "[" + plugin.getName() + "] ";

        Bukkit.getConsoleSender().sendMessage(text + ChatColor.WHITE + message);

        if (level == LogLevel.DEBUG) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("phantom.logging." + level.toString().toLowerCase())) {
                player.sendMessage(text + ChatColor.WHITE + message);
            }
        }
    }

    public enum LogLevel {DEBUG, ERROR, FATAL, INFO, WARN, SUCCESS}
}
