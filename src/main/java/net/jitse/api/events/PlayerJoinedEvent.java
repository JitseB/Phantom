package net.jitse.api.events;

import net.jitse.api.account.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJoinedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Account account;

    public PlayerJoinedEvent(Player player, Account account) {
        this.player = player;
        this.account = account;
    }

    public Player getPlayer() {
        return player;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

