package net.jitse.phantom.spigot.managers;

import net.jitse.api.account.rank.Rank;
import net.jitse.api.configuration.Config;
import net.jitse.api.logging.Logger;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.account.rank.PhantomRank;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RanksManager {

    private final Phantom plugin;
    private final Set<Rank> ranks;

    private Rank defaultRank;

    public RanksManager(Phantom plugin) {
        this.plugin = plugin;
        this.ranks = new HashSet<>();
    }

    public boolean load(Config ranksConfig) {
        Set<String> section = ranksConfig.getConfigurationSection("").getKeys(false);
        for (String name : section) {
            if (name.length() > 16) {
                Logger.log(plugin, Logger.LogLevel.WARN, "Tried to load rank " + name + ", but it has an invalid " +
                        "name. It should be no longer than 16 characters. Skipping it...");
                continue;
            }

            boolean operator = ranksConfig.getBoolean(name + ".Operator");
            String prefix = ranksConfig.getString(name + ".Prefix");
            String chatFormat = ranksConfig.getString(name + ".ChatFormat");
            int level;
            try {
                level = ranksConfig.getInt(name + ".Level");
            } catch (NumberFormatException exception) {
                Logger.log(plugin, Logger.LogLevel.WARN, "Tried to load rank " + name + ", but it has an invalid " +
                        "level value. Skipping it...");
                continue;
            }

            Rank rank = new PhantomRank(name, prefix, chatFormat, level, operator);
            ranks.add(rank);
        }

        Logger.log(plugin, Logger.LogLevel.DEBUG, "Loaded " + ranks.size() + " ranks from ranks.yml.");

        this.defaultRank = getRank(plugin.getSettingsConfig().getString("DefaultRank")).orElse(null);
        if (defaultRank == null) {
            Logger.log(plugin, Logger.LogLevel.FATAL, "Default rank does not exist is the ranks.yml.");
            return false;
        }

        Logger.log(plugin, Logger.LogLevel.DEBUG, "Set \"" + defaultRank.getName() + "\" as the default rank.");
        return true;
    }

    public Optional<Rank> getRank(String name) {
        return ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst();
    }

    public Rank getDefaultRank() {
        return defaultRank;
    }

    public Set<Rank> getAllRanks() {
        return ranks;
    }
}
