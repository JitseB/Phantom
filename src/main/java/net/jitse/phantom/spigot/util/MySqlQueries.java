package net.jitse.phantom.spigot.util;

public class MySqlQueries {

    public static String CREATE_PHANTOM_ACCOUNTS_TABLE = "CREATE TABLE IF NOT EXISTS PhantomAccounts (" +
            "UUID VARCHAR(36) PRIMARY KEY NOT NULL, Name VARCHAR(16) NOT NULL, Rank VARCHAR(16) NOT NULL, " +
            "FirstJoin BIGINT(50) NOT NULL, LastJoin BIGINT(50) NOT NULL, PlayTime BIGINT(50) NOT NULL);";

    public static String SELECT_ACCOUNT_FROM_UUID = "SELECT UUID, Name, Rank, FirstJoin, LastJoin, PlayTime FROM PhantomAccounts WHERE UUID=?;";

    public static String INSERT_ACCOUNT = "INSERT INTO PhantomAccounts VALUES(?,?,?,?,?,?);";

    public static String UPDATE_VALUE = "UPDATE PhantomAccounts SET %field%=? WHERE UUID=?;";
}
