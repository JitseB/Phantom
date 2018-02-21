package net.jitse.phantom.spigot.utilities.logging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SpigotLogger {

    private final String name;
    private final ChatColor nameColor, messageColor;
    private final LogLevel max;

    public SpigotLogger(String name, ChatColor nameColor, ChatColor messageColor, LogLevel max) {
        this.name = name;
        this.nameColor = nameColor;
        this.messageColor = messageColor;
        this.max = max;
    }

    public void log(LogLevel level, String message) {
        if (level.ordinal() < max.ordinal()) {
            return;
        }

        String text = nameColor + "[" + name + "] ";
        switch (level) {
            case DEBUG:
                text += ChatColor.YELLOW + "[Debug] ";
                break;
            case ERROR:
                text += ChatColor.RED + "[Error] ";
                break;
            case FATAL:
                text += ChatColor.RED + "[Fatal] ";
                break;
            case INFO:
                text += ChatColor.WHITE + "[Info] ";
                break;
            case WARN:
                text += ChatColor.GOLD + "[Warn] ";
                break;
            case SUCCESS:
                text += ChatColor.GREEN + "[Success] ";
                break;
            default:
                text += "";
                break;
        }

        Bukkit.getConsoleSender().sendMessage(text + messageColor + message);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("phantom.logging." + level.toString().toLowerCase())) {
                player.sendMessage(text + messageColor + message);
            }
        }
    }
}
