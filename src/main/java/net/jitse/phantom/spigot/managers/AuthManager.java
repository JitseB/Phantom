package net.jitse.phantom.spigot.managers;

import net.jitse.api.account.rank.AuthType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthManager {

    private final Map<UUID, AuthType> active;

    public AuthManager() {
        this.active = new HashMap<>();
    }

    public boolean isAuthenticating(Player player) {
        return active.containsKey(player.getUniqueId());
    }

    public AuthType getType(Player player) {
        return active.get(player.getUniqueId());
    }

    public void add(UUID uuid, AuthType authType) {
        active.put(uuid, authType);
    }

    public void remove(UUID uuid) {
        active.remove(uuid);
    }

    public void removeSafe(UUID uuid) {
        if (!active.containsKey(uuid)) {
            return;
        }
        active.remove(uuid);
    }
}
