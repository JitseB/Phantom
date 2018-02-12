package net.jitse.phantom.spigot.account.type;

import net.jitse.phantom.spigot.account.Account;
import net.jitse.phantom.spigot.account.rank.Rank;
import org.bukkit.entity.Player;

import java.util.UUID;

public class OfflineAccount implements Account {

    private final UUID uuid;
    private final String name;
    private Rank rank;

    public OfflineAccount(UUID uuid, String name, Rank rank) {
        this.uuid = uuid;
        this.name = name;
        this.rank = rank;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public Player getPlayer() {
        return null;
    }
}
