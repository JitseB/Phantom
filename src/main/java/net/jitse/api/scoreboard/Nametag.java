package net.jitse.api.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Nametag {

    public static void set(Player player, NametagType type, String text) {
        Scoreboard playerScoreboard = player.getScoreboard();

        for (Player online : Bukkit.getOnlinePlayers()) {
            Scoreboard onlineScoreboard = online.getScoreboard();
            Team playerTeam = getOrCreateTeam(onlineScoreboard, player.getName());
            switch (type) {
                case PREFIX:
                    playerTeam.setPrefix(text);
                    break;
                case SUFFIX:
                    playerTeam.setSuffix(text);
                    break;
                case BOTH:
                    playerTeam.setPrefix(text);
                    playerTeam.setSuffix(text);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid name tag type.");
            }

            // Now add the player (if not already on the team).
            if (!playerTeam.getEntries().contains(player.getName())) {
                playerTeam.addEntry(player.getName());
            }

            // Now update the player's scoreboard with the online's team (if existing).
            Team onlineTeamSelf = onlineScoreboard.getTeam(online.getName());
            if (onlineTeamSelf != null) {
                Team onlineTeam = getOrCreateTeam(playerScoreboard, online.getName());
                onlineTeam.setPrefix(onlineTeamSelf.getPrefix());
                onlineTeam.setSuffix(onlineTeamSelf.getSuffix());

                // Now add the online player (if not already on the team).
                if (!onlineTeam.getEntries().contains(online.getName())) {
                    onlineTeam.addEntry(online.getName());
                }
            }
        }
    }

    private static Team getOrCreateTeam(Scoreboard scoreboard, String name) {
        Team team = scoreboard.getTeam(name);
        if (team == null) {
            team = scoreboard.registerNewTeam(name);
        }
        return team;
    }

    public enum NametagType { PREFIX, SUFFIX, BOTH }
}
