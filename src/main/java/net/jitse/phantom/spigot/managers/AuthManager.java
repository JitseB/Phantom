package net.jitse.phantom.spigot.managers;

import net.jitse.api.account.rank.AuthType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthManager {

    private final Map<UUID, Map.Entry<AuthType, String>> active;

    public AuthManager() {
        this.active = new HashMap<>();
    }

    public boolean isAuthenticating(Player player) {
        return active.containsKey(player.getUniqueId());
    }

    public Map.Entry<AuthType, String> get(Player player) {
        return active.get(player.getUniqueId());
    }
}
