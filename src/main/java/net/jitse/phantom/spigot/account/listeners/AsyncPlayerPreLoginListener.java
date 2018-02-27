package net.jitse.phantom.spigot.account.listeners;

import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.account.Account;
import net.jitse.phantom.spigot.exceptions.AccountFetchFailedException;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class AsyncPlayerPreLoginListener implements Listener {

    private final Phantom plugin;

    public AsyncPlayerPreLoginListener(Phantom plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        try {
            Account account = plugin.getStorageManager().getRedisStorage().getAccount(uuid);
            if (account == null) {
                account = plugin.getStorageManager().getSqlStorage().getAccount(uuid);
            }

            // Everything failed -> Kick the user.
            if (account == null) {
                event.setKickMessage(ChatColor.RED + "Phantom failed to load your account." + ChatColor.RESET + "\n\n"
                        + ChatColor.WHITE + "If this keeps happening, please report this" + ChatColor.RESET + "\n"
                        + ChatColor.WHITE + "to your server administrator." + ChatColor.RESET);
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            }
        } catch (AccountFetchFailedException exception) {
            event.setKickMessage(ChatColor.RED + "Phantom failed to load your account." + ChatColor.RESET + "\n\n"
                    + ChatColor.WHITE + "If this keeps happening, please report this" + ChatColor.RESET + "\n"
                    + ChatColor.WHITE + "to your server administrator." + ChatColor.RESET + "\n\n"
                    + (exception.hasMessage() ? ChatColor.WHITE + exception.getMessage() + ChatColor.RESET + "\n\n" : "")
                    + ChatColor.DARK_GRAY + "Alternatively, you can open an issue on GitHub:" + ChatColor.RESET + "\n"
                    + ChatColor.RED.toString() + ChatColor.UNDERLINE + "https://github.com/JitseB/phantom/issues" + ChatColor.RESET);
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }
}
