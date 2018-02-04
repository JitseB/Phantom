package net.jitse.api.account;

import net.jitse.api.account.rank.Rank;

import java.util.UUID;

public interface Account {

    UUID getUniqueId();

    String getName();

    Rank getRank();

    void setRank(Rank rank);
}
