package net.jitse.phantom.spigot.listeners.account;

import net.jitse.api.account.Account;
import net.jitse.api.events.PlayerJoinedEvent;
import net.jitse.api.scoreboard.Nametag;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.listeners.BaseListener;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.permissions.PermissionAttachment;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PlayerJoinedListener extends BaseListener {

    private final String gitHubLatestRelease;
    private final JSONParser jsonParser;

    public PlayerJoinedListener(Phantom plugin) {
        super(plugin);

        this.gitHubLatestRelease = "https://api.github.com/repos/JitseB/phantom/releases/latest";
        this.jsonParser = new JSONParser();
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

        // Make op (if auto-op is enabled).
        if (getPlugin().getSettingsConfig().getBoolean("AutoOperator")) {
            player.setOp(account.getRank().isOperator());
        }

        // Phantom version checker (if operator).
        if (player.isOp() && getPlugin().getSettingsConfig().getBoolean("UpdateNotifier")) {
            checkVersion(player);
        }


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

        for (String line : getPlugin().getMessagesConfig().getStringList("WelcomeMessage")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', line.replace("%player_name%", player.getName())));
        }
    }

    private void checkVersion(Player operator) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(gitHubLatestRelease).openConnection();
            JSONObject response = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
            if (response.containsKey("tag_name")) {
                String latest = (String) response.get("tag_name");
                String url = (String) response.get("html_url");
                String self = "v" + getPlugin().getDescription().getVersion();

                if (!latest.equals(self)) {
                    BaseComponent base = new TextComponent("There is an update available for Phantom! It is recommended " +
                            "to update as soon as possible. Latest release: ");
                    base.setColor(net.md_5.bungee.api.ChatColor.RED);

                    BaseComponent version = new TextComponent(latest);
                    version.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("View update")}));
                    version.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                    version.setColor(net.md_5.bungee.api.ChatColor.WHITE);
                    base.addExtra(version);

                    BaseComponent dot = new TextComponent(".");
                    dot.setColor(net.md_5.bungee.api.ChatColor.RED);
                    base.addExtra(dot);

                    operator.spigot().sendMessage(base);
                }
            } else {
                operator.sendMessage(ChatColor.RED + "Failed to check latest version of Phantom.");
            }
        } catch (Exception exception) {
            operator.sendMessage(ChatColor.RED + "Failed to check latest version of Phantom.");
        }
    }

}
