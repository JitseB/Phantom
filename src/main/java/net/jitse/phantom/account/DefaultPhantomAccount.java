package net.jitse.phantom.account;

import net.jitse.api.account.rank.Rank;

import java.util.UUID;

public class DefaultPhantomAccount extends PhantomAccount {

    public DefaultPhantomAccount(Rank defaultRank, UUID uuid, String name) {
        super(uuid, name, defaultRank);
    }
}
