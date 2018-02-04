package net.jitse.phantom.listeners.account;

import net.jitse.api.account.rank.Rank;
import net.jitse.api.events.AccountRankChangedEvent;
import net.jitse.api.scoreboard.Nametag;
import net.jitse.api.storage.AccountField;
import net.jitse.phantom.Phantom;
import net.jitse.phantom.listeners.BaseListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class AccountRankChangedListener extends BaseListener {

    public AccountRankChangedListener(Phantom plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAccountRankChanged(AccountRankChangedEvent event) {
        Player player = event.getPlayer();
        Rank before = event.getBefore();
        Rank after = event.getAfter();
        boolean upgrade = after.getLevel() >= before.getLevel();

        getPlugin().getStorage().update(player.getUniqueId(), AccountField.RANK, after.getName());

        // If using Redis -> Update it.
        if (getPlugin().useRedis()) {
            getPlugin().getRedis().update(player.getUniqueId(), AccountField.RANK, after.getName());
        }

        Nametag.set(player, Nametag.NametagType.PREFIX, ChatColor.translateAlternateColorCodes('&', after.getPrefix()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                getPlugin().getMessagesConfig().getString("Account.Rank." + (upgrade ? "Upgrade" : "Downgrade"))
                        .replace("%rank_name%", after.getName())
        ));
    }
}
