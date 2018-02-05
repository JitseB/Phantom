package net.jitse.phantom.spigot.managers;

import net.jitse.api.account.Account;
import net.jitse.api.exceptions.AccountFetchFailedException;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.logging.SpigotLogger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountManager {

    private final Phantom plugin;
    private final Map<UUID, Account> accounts;

    public AccountManager(Phantom plugin) {
        this.plugin = plugin;
        this.accounts = new HashMap<>();
    }

    public void loadAll() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Account account;
            try {
                account = plugin.getStorage().getAccount(player.getUniqueId());
                add(player.getUniqueId(), account);
            } catch (AccountFetchFailedException exception) {
                // If failed -> Kick the user.
                player.kickPlayer(ChatColor.RED + "Phantom failed to reload your account." + ChatColor.RESET + "\n\n"
                        + ChatColor.WHITE + "If this keeps happening, please report this" + ChatColor.RESET + "\n"
                        + ChatColor.WHITE + "to your server administrator." + ChatColor.RESET + "\n\n"
                        + (exception.hasMessage() ? ChatColor.WHITE + exception.getMessage() + ChatColor.RESET + "\n\n" : "")
                        + ChatColor.DARK_GRAY + "Alternatively, you can open an issue on GitHub:" + ChatColor.RESET + "\n"
                        + ChatColor.RED.toString() + ChatColor.UNDERLINE + "https://github.com/JitseB/phantom/issues" + ChatColor.RESET);
                SpigotLogger.log(plugin, SpigotLogger.LogLevel.WARN, "Could not reload account of " + player.getName() + ".");
            }
        }
    }

    public void add(UUID uuid, Account account) {
        accounts.put(uuid, account);
    }

    public boolean has(UUID uuid) {
        return accounts.containsKey(uuid);
    }

    public Account getAccount(UUID uuid) {
        return accounts.get(uuid);
    }
}
