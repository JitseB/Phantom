package net.jitse.api.events;

import net.jitse.api.account.Account;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AccountLoadedAsyncEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Account account;

    public AccountLoadedAsyncEvent(Account account) {
        this.account = account;
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

