package net.jitse.phantom.spigot.listeners.player;

import net.jitse.api.account.Account;
import net.jitse.api.account.rank.AuthType;
import net.jitse.api.auth.AuthValidator;
import net.jitse.api.exceptions.HashNotPresentException;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.listeners.BaseListener;
import net.jitse.phantom.spigot.logging.SpigotLogger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener extends BaseListener {

    private final Pattern boldPattern;
    private final Pattern italicPattern;
    private final Pattern underlinePattern;
    private final Pattern strikethroughPattern;

    public ChatListener(Phantom plugin) {
        super(plugin);

        this.boldPattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
        this.italicPattern = Pattern.compile("\\*(.*?)\\*");
        this.underlinePattern = Pattern.compile("__(.*?)__");
        this.strikethroughPattern = Pattern.compile("~~(.*?)~~");
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!getPlugin().getAccountManager().has(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Phantom didn't load your account yet, try again in a bit...");
            event.setCancelled(true);
            return;
        }

        // If player is in authentication mode -> Cancel the event.
        if (getPlugin().getAuthManager().isAuthenticating(player)) {
            AuthType authType = getPlugin().getAuthManager().getType(player);
            if (authType == AuthType.PHRASE) {
                getPlugin().getServer().getScheduler().runTaskAsynchronously(getPlugin(), () -> {
                    try {
                        String hash = getPlugin().getStorage().getHashedAuthenticator(player.getUniqueId());
                        if (new AuthValidator().validate(hash, event.getMessage())) {
                            player.sendMessage(ChatColor.GREEN + "Access granted. Welcome back.");
                            getPlugin().getAuthManager().remove(player);
                        } else {
                            player.sendMessage(ChatColor.RED + "Wrong phrase. Try again...");
                        }
                    } catch (HashNotPresentException | NoSuchAlgorithmException exception) {
                        player.sendMessage("Todo - Exception");
                    }
                });
            }
            event.setCancelled(true);
            return;
        }

        Account account = getPlugin().getAccountManager().getAccount(player.getUniqueId());
        String format = ChatColor.translateAlternateColorCodes('&', account.getRank().getChatFormat().replace("%prefix%", account.getRank().getPrefix()))
                .replaceFirst("%player_name%", "%s")
                .replaceFirst("%message%", "%s");

        if (StringUtils.countMatches(format, "%s") != 2) {
            SpigotLogger.log(getPlugin(), SpigotLogger.LogLevel.WARN, "Couldn't parse " + player.getName() + "'s chat format. " +
                    "It didn't contain the \"%user_name%\" and \"%message%\" parameters!");
            player.sendMessage(ChatColor.RED + "Phantom wasn't able to parse your chat format, please contact an administrator.");
            event.setCancelled(true);
            return;
        }

        if (player.hasPermission("phantom.chat.color")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }

        if (player.hasPermission("phantom.chat.markup")) {
            event.setMessage(markup(event.getMessage(), ChatColor.getLastColors(format)));
        }

        event.setFormat(format);
    }

    // Todo: Clean up this mess.
    private String markup(String input, String lastColors) {
        StringBuffer buffer = new StringBuffer(input.length());

        Matcher boldMatcher = boldPattern.matcher(input);
        while (boldMatcher.find()) {
            String text = boldMatcher.group(1);
            boldMatcher.appendReplacement(buffer, Matcher.quoteReplacement(ChatColor.BOLD + text + ChatColor.RESET + lastColors));
        }
        boldMatcher.appendTail(buffer);
        input = buffer.toString();
        buffer = new StringBuffer(input.length());

        Matcher italicMatcher = italicPattern.matcher(input);
        while (italicMatcher.find()) {
            String text = italicMatcher.group(1);
            italicMatcher.appendReplacement(buffer, Matcher.quoteReplacement(ChatColor.ITALIC + text + ChatColor.RESET + lastColors));
        }
        italicMatcher.appendTail(buffer);
        input = buffer.toString();
        buffer = new StringBuffer(input.length());

        Matcher underlineMatcher = underlinePattern.matcher(input);
        while (underlineMatcher.find()) {
            String text = underlineMatcher.group(1);
            underlineMatcher.appendReplacement(buffer, Matcher.quoteReplacement(ChatColor.UNDERLINE + text + ChatColor.RESET + lastColors));
        }
        underlineMatcher.appendTail(buffer);
        input = buffer.toString();
        buffer = new StringBuffer(input.length());

        Matcher strikethroughMatcher = strikethroughPattern.matcher(input);
        while (strikethroughMatcher.find()) {
            String text = strikethroughMatcher.group(1);
            strikethroughMatcher.appendReplacement(buffer, Matcher.quoteReplacement(ChatColor.STRIKETHROUGH + text + ChatColor.RESET + lastColors));
        }
        strikethroughMatcher.appendTail(buffer);

        return buffer.toString();
    }
}
