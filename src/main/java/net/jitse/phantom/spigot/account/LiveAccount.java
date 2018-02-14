package net.jitse.phantom.spigot.account;

import org.bukkit.entity.Player;

public interface LiveAccount extends Account {

    /**
     * @return Returns the player (if online, otherwise null).
     */
    Player getPlayer();
}
