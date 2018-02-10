package net.jitse.phantom.spigot.account.rank;

import net.jitse.phantom.spigot.modules.auth.type.AuthType;

public interface Rank {

    String getName();

    String getPrefix();

    String getChatFormat();

    int getLevel();

    boolean isOperator();

    AuthType getAuthentication();
}
