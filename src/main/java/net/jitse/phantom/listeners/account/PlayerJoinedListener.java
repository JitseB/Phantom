package net.jitse.phantom.listeners.account;

import net.jitse.api.account.Account;
import net.jitse.api.events.PlayerJoinedEvent;
import net.jitse.api.scoreboard.Nametag;
import net.jitse.phantom.Phantom;
import net.jitse.phantom.listeners.BaseListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;

public class PlayerJoinedListener extends BaseListener {

    public PlayerJoinedListener(Phantom plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoined(PlayerJoinedEvent event) {
        Player player = event.getPlayer();
        Account account = event.getAccount();

        // Add permissions (if existing).
        if (getPlugin().getPermissionsConfig().get(account.getRank().getName()) != null) {
            List<String> permissions = getPlugin().getPermissionsConfig().getStringList(account.getRank().getName());
            PermissionAttachment attachment = player.addAttachment(getPlugin());

            if (permissions.size() > 0) {
                for (String permission : permissions) {
                    attachment.setPermission(permission, true);
                }
            }
        }

        // Apply name tag.
        Nametag.set(player, Nametag.NametagType.PREFIX, ChatColor.translateAlternateColorCodes('&', account.getRank().getPrefix()));
        Nametag.set(player, Nametag.NametagType.SUFFIX, "");

        // Send join message.

        String joinMessage = ChatColor.translateAlternateColorCodes('&', getPlugin().getMessagesConfig().getString("JoinMessage")
                .replace("%player_name%", player.getName())
                .replace("%prefix%", account.getRank().getPrefix())
        );
        if (joinMessage.length() > 0) {
            for (Player online : getPlugin().getServer().getOnlinePlayers()) {
                online.sendMessage(joinMessage);
            }
        }

        if (getPlugin().getSettingsConfig().getBoolean("AutoOperator")) {
            player.setOp(account.getRank().isOperator());
        }

        player.sendMessage(ChatColor.YELLOW + "Hey there!");
    }
}
