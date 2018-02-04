package net.jitse.api.events;

import net.jitse.api.account.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AccountRankChangedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Rank before, after;

    public AccountRankChangedEvent(Player player, Rank before, Rank after) {
        this.player = player;
        this.before = before;
        this.after = after;
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getBefore() {
        return before;
    }

    public Rank getAfter() {
        return after;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
