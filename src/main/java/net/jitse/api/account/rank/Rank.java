package net.jitse.api.account.rank;

public interface Rank {

    String getName();

    String getPrefix();

    String getChatFormat();

    int getLevel();

    boolean isOperator();

    AuthType getAuthentication();
}
