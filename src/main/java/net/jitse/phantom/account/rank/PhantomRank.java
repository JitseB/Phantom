package net.jitse.phantom.account.rank;

import net.jitse.api.account.rank.Rank;

public class PhantomRank implements Rank {

    private final String name, prefix, chatFormat;
    private final int level;
    private final boolean operator;

    public PhantomRank(String name, String prefix, String chatFormat, int level, boolean operator) {
        this.name = name;
        this.prefix = prefix;
        this.chatFormat = chatFormat;
        this.level = level;
        this.operator = operator;
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
}
