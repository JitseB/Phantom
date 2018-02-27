package net.jitse.phantom.spigot.account;

import net.jitse.phantom.spigot.account.rank.Rank;

import java.util.UUID;

public interface Account {

    /**
     * @return The unique id of the player.
     */
    UUID getUniqueId();

    /**
     * @return The name of the player.
     */
    String getName();

    /**
     * @return The rank of the player.
     */
    Rank getRank();

    /**
     * Change the player's rank.
     *
     * @param rank The new rank for the player.
     */
    void setRank(Rank rank);

    /**
     * @return Whether the player is online.
     */
    boolean isOnline();

    /**
     * @return A cloned object of the account.
     */
    Account clone();
}
