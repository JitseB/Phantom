package net.jitse.phantom.spigot.account.type;

import net.jitse.phantom.spigot.account.LiveAccount;
import net.jitse.phantom.spigot.account.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OnlineAccount extends OfflineAccount implements LiveAccount {

    public OnlineAccount(Player player, Rank rank) {
        super(player.getUniqueId(), player.getName(), rank);
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }
}
