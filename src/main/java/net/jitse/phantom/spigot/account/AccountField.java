package net.jitse.phantom.spigot.account;

public enum AccountField {

    UUID("UUID", false, true),
    NAME("Name", true, true),
    RANK("Rank", true, true),
    FIRST_JOIN("FirstJoin", false, true),
    LAST_JOIN("LastJoin", true, true),
    PLAY_TIME("PlayTime", true, true);

    private final String column;
    private final boolean set, get;

    AccountField(String column, boolean set, boolean get) {
        this.column = column;
        this.set = set;
        this.get = get;
    }

    public String getSqlColumn() {
        return column;
    }

    public boolean canSet() {
        return set;
    }

    public boolean canGet() {
        return get;
    }
}
