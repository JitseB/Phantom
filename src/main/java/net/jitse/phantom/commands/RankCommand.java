package net.jitse.phantom.commands;

import net.jitse.api.account.Account;
import net.jitse.api.account.rank.Rank;
import net.jitse.phantom.Phantom;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class RankCommand implements CommandExecutor {

    private final Phantom plugin;

    public RankCommand(Phantom plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("phantom.commands.rank")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.NoPermission")
                    .replace("%permission%", "phantom.commands.rank")));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.Usage")
                    .replace("%syntax%", "/setrank <player> <rank: "
                            + StringUtils.join(plugin.getRanksManager().getAllRanks().stream().map(Rank::getName).collect(Collectors.toSet()), ", ")
                            + ">")));
            return true;
        }

        Rank rank = plugin.getRanksManager().getRank(args[1]).orElse(null);
        if (rank == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.Rank.Invalid")
                    .replace("%argument%", args[1])));
            return true;
        }

        Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null || !player.isOnline()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("Command.Player.Offline")
                    .replace("%player_name%", args[0])));
            return true;
        }

        Account account = plugin.getAccountManager().getAccount(player.getUniqueId());
        account.setRank(rank);
        return true;
    }
}
