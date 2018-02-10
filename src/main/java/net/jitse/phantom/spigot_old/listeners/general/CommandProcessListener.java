package net.jitse.phantom.spigot_old.listeners.general;

import net.jitse.phantom.spigot_old.Phantom;
import net.jitse.phantom.spigot_old.listeners.BaseListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class CommandProcessListener extends BaseListener {

    public CommandProcessListener(Phantom plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0].substring(1);

        // Prevent infinite loop.
        if (command.startsWith("phantom:")) {
            return;
        }

        if (getPlugin().getCommand(command) != null) {
            event.setCancelled(true);
            player.performCommand("phantom:" + command + event.getMessage().replace("/" + command, ""));
        }
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand();

        if (command.startsWith("phantom:")) {
            return;
        }

        if (getPlugin().getCommand(command) != null) {
            event.setCommand("phantom:" + command);
        }
    }
}
