package net.jitse.phantom.spigot.utilities.logging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotLogger {

    private final JavaPlugin plugin;
    private final LogLevel max;

    public SpigotLogger(JavaPlugin plugin, LogLevel max) {
        this.plugin = plugin;
        this.max = max;
    }

    public void log(LogLevel level, String message) {
        if (level.ordinal() > max.ordinal()) {
            return;
        }

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

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("phantom.logging." + level.toString().toLowerCase())) {
                player.sendMessage(text + ChatColor.WHITE + message);
            }
        }
    }
}
