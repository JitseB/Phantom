package net.jitse.phantom.spigot_old.listeners.player;

import net.jitse.phantom.spigot_old.Phantom;
import net.jitse.phantom.spigot_old.listeners.BaseListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener extends BaseListener {

    public PlayerMoveListener(Phantom plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        boolean moved = event.getFrom().getX() != event.getTo().getX()
                || event.getFrom().getZ() != event.getTo().getZ()
                || event.getFrom().getY() != event.getTo().getY();

        if (getPlugin().getAuthManager().isAuthenticating(player) && moved) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', getPlugin().getMessagesConfig().getString("Auth.MoveBlock")));
            event.setCancelled(true);
        }
    }
}
