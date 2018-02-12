package net.jitse.phantom.spigot.account.rank;

import net.jitse.phantom.spigot.modules.auth.type.AuthType;

public interface Rank {

    /**
     * @return The name of the rank.
     */
    String getName();

    /**
     * @return The prefix of the rank (formatted with ChatColor).
     */
    String getPrefix();

    /**
     * @return The chat format of the rank.
     */
    String getChatFormat();

    /**
     * @return The level of the rank (Works similar to {@link Enum#ordinal}.
     */
    int getLevel();

    /**
     * @return Whether the rank is supposed to be an operator.
     */
    boolean isOperator();

    /**
     * @return The authentication type the rank requires.
     */
    AuthType getAuthentication();
}
