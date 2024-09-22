package net.jitse.phantom.spigot.utilities.logging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpigotLogger {

    private static final Logger LOGGER = Bukkit.getLogger();
    private static final Map<LogLevel, String> LOG_LEVEL_PREFIXES = new EnumMap<>(LogLevel.class);

    static {
        LOG_LEVEL_PREFIXES.put(LogLevel.DEBUG, ChatColor.YELLOW + "[Debug] ");
        LOG_LEVEL_PREFIXES.put(LogLevel.ERROR, ChatColor.RED + "[Error] ");
        LOG_LEVEL_PREFIXES.put(LogLevel.FATAL, ChatColor.RED + "[Fatal] ");
        LOG_LEVEL_PREFIXES.put(LogLevel.INFO, ChatColor.WHITE + "[Info] ");
        LOG_LEVEL_PREFIXES.put(LogLevel.WARN, ChatColor.GOLD + "[Warn] ");
        LOG_LEVEL_PREFIXES.put(LogLevel.SUCCESS, ChatColor.GREEN + "[Success] ");
    }

    private final String prefix;
    private final ChatColor messageColor;
    private final LogLevel maxLevel;

    public SpigotLogger(String name, ChatColor nameColor, ChatColor messageColor, LogLevel maxLevel) {
        this.prefix = nameColor + "[" + name + "] ";
        this.messageColor = messageColor;
        this.maxLevel = maxLevel;
    }

    public void log(LogLevel level, String message) {
        if (level.ordinal() < maxLevel.ordinal()) {
            return;
        }

        String levelPrefix = LOG_LEVEL_PREFIXES.getOrDefault(level, "");
        String fullMessage = prefix + levelPrefix + messageColor + message;

        LOGGER.log(getJavaLogLevel(level), ChatColor.stripColor(fullMessage));

        String permission = "phantom.logging." + level.toString().toLowerCase();
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(fullMessage));
    }

    private Level getJavaLogLevel(LogLevel level) {
        switch (level) {
            case DEBUG:
                return Level.FINE;
            case ERROR:
            case FATAL:
                return Level.SEVERE;
            case WARN:
                return Level.WARNING;
            case SUCCESS:
            case INFO:
            default:
                return Level.INFO;
        }
    }
}
