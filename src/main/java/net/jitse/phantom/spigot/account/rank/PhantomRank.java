package net.jitse.phantom.spigot.account.rank;


import net.jitse.phantom.spigot.modules.auth.type.AuthType;

public class PhantomRank implements Rank {

    private final String name, prefix, chatFormat;
    private final int level;
    private final boolean operator;
    private final AuthType authType;

    public PhantomRank(String name, String prefix, String chatFormat, int level, boolean operator, AuthType authType) {
        this.name = name;
        this.prefix = prefix;
        this.chatFormat = chatFormat;
        this.level = level;
        this.operator = operator;
        this.authType = authType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getChatFormat() {
        return chatFormat;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public boolean isOperator() {
        return operator;
    }

    public AuthType getAuthentication() {
        return authType;
    }
}
