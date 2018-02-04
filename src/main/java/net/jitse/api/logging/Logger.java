package net.jitse.api.logging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Logger {

    public static void log(JavaPlugin plugin, LogLevel level, String message) {
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
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (op.isOp()) {
                op.sendMessage(text + ChatColor.WHITE + message);
            }
        }
    }

    public enum LogLevel { DEBUG, ERROR, FATAL, INFO, WARN, SUCCESS }
}
