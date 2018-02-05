package net.jitse.phantom.spigot.listeners.general;

import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.listeners.BaseListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener extends BaseListener {

    public ServerListPingListener(Phantom plugin) {
        super(plugin);
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String motd = getPlugin().getMessagesConfig().getString("ServerListMessage");
        event.setMotd(ChatColor.translateAlternateColorCodes('&', motd));
        event.setMaxPlayers(getPlugin().getSettingsConfig().getInt("MaxPlayers"));
    }
}
