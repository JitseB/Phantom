package net.jitse.phantom.spigot.listeners.player;

import net.jitse.api.account.Account;
import net.jitse.api.events.AccountLoadedAsyncEvent;
import net.jitse.api.logging.Logger;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.account.DefaultPhantomAccount;
import net.jitse.phantom.spigot.exceptions.AccountFetchFailedException;
import net.jitse.phantom.spigot.listeners.BaseListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener extends BaseListener {

    public AsyncPlayerPreLoginListener(Phantom plugin) {
        super(plugin);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Account account = null;
        Logger.log(getPlugin(), Logger.LogLevel.DEBUG, "Using Redis=" + getPlugin().useRedis()
                + ", on async thread=" + event.isAsynchronous() + ".");

        boolean fromRedis = false;
        if (getPlugin().useRedis()) {
            // Get account from Redis server.
            try {
                account = getPlugin().getRedis().getAccount(event.getUniqueId());
                fromRedis = true;
            } catch (AccountFetchFailedException ignored) {
                // If failed -> Try the database.
            }
        }

        // If server doesn't use Redis or it failed, grab it from the database.
        if (account == null) {
            try {
                account = getPlugin().getStorage().getAccount(event.getUniqueId());
            } catch (AccountFetchFailedException exception) {
                // If failed -> Kick the user.
                event.setKickMessage(ChatColor.RED + "Phantom failed to load your account." + ChatColor.RESET + "\n\n"
                        + ChatColor.WHITE + "If this keeps happening, please report this" + ChatColor.RESET + "\n"
                        + ChatColor.WHITE + "to your server administrator." + ChatColor.RESET + "\n\n"
                        + (exception.hasMessage() ? ChatColor.WHITE + exception.getMessage() + ChatColor.RESET + "\n\n" : "")
                        + ChatColor.DARK_GRAY + "Alternatively, you can open an issue on GitHub:" + ChatColor.RESET + "\n"
                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + "https://github.com/JitseB/phantom/issues" + ChatColor.RESET);
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                Logger.log(getPlugin(), Logger.LogLevel.WARN, "Could not load account of " + event.getName() + ".");
            }
        }

        // If everything failed, end the connection.
        if (account == null) {
            account = new DefaultPhantomAccount(getPlugin().getRanksManager().getDefaultRank(), event.getUniqueId(), event.getName());

            // Store new account in database.
            boolean success = getPlugin().getStorage().storeAccount(account);
            if (!success) {
                Logger.log(getPlugin(), Logger.LogLevel.WARN, "Could not store " + event.getName() + "'s account in the database.");
            }
        }

        // Save the account in the Redis server.
        if (!fromRedis && getPlugin().useRedis()) {
            boolean success = getPlugin().getStorage().storeAccount(account);
            if (!success) {
                Logger.log(getPlugin(), Logger.LogLevel.WARN, "Could not store " + event.getName() + "'s account in the Redis server.");
            }
        }

        getPlugin().getAccountManager().add(event.getUniqueId(), account);
        getPlugin().getServer().getPluginManager().callEvent(new AccountLoadedAsyncEvent(account));
    }
}
