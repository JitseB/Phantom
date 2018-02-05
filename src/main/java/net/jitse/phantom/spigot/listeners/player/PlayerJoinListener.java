package net.jitse.phantom.spigot.listeners.player;

import net.jitse.api.account.Account;
import net.jitse.api.events.PlayerJoinedEvent;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.listeners.BaseListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener extends BaseListener {

    public PlayerJoinListener(Phantom plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Set join message to null -> We want to send it on the PlayerJoinedEvent (Account provided).
        event.setJoinMessage(null);

        // Account loaded event.
        if (getPlugin().getAccountManager().has(player.getUniqueId())) {
            Account account = getPlugin().getAccountManager().getAccount(player.getUniqueId());
            PlayerJoinedEvent call = new PlayerJoinedEvent(player, account);
            getPlugin().getServer().getPluginManager().callEvent(call);
        } else {
            // Otherwise start a runnable waiting for the account to be loaded.
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        this.cancel();
                        return;
                    }

                    if (getPlugin().getAccountManager().has(player.getUniqueId())) {
                        Account account = getPlugin().getAccountManager().getAccount(player.getUniqueId());
                        PlayerJoinedEvent call = new PlayerJoinedEvent(player, account);
                        // Now call the event on the main thread.
                        Bukkit.getScheduler().runTask(getPlugin(), () -> getPlugin().getServer().getPluginManager().callEvent(call));
                        this.cancel();
                    }
                }

            }.runTaskTimerAsynchronously(getPlugin(), 0, 1);
        }
    }
}
