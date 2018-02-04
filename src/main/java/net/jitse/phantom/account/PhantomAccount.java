package net.jitse.phantom.account;

import net.jitse.api.account.Account;
import net.jitse.api.account.rank.Rank;
import net.jitse.api.events.AccountRankChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PhantomAccount implements Account {

    private final UUID uuid;
    private final String name;

    private Rank rank;

    public PhantomAccount(UUID uuid, String name, Rank rank) {
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
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            AccountRankChangedEvent event = new AccountRankChangedEvent(player, this.rank, rank);
            Bukkit.getPluginManager().callEvent(event);
        }

        this.rank = rank;
    }
}
