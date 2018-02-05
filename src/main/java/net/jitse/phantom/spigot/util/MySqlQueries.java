package net.jitse.phantom.spigot.util;

public class MySqlQueries {

    // PhantomAccount
    public static String ACCOUNT_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS PhantomAccount (" +
            "UUID VARCHAR(36) PRIMARY KEY NOT NULL, Name VARCHAR(16) NOT NULL, Rank VARCHAR(16) NOT NULL, " +
            "FirstJoin BIGINT(50) NOT NULL, LastJoin BIGINT(50) NOT NULL, PlayTime BIGINT(50) NOT NULL);";

    public static String ACCOUNT_GET_FROM_UUID = "SELECT UUID, Name, Rank, FirstJoin, LastJoin, PlayTime FROM PhantomAccount WHERE UUID=?;";

    public static String ACCOUNT_INSERT_NEW = "INSERT INTO PhantomAccount VALUES(?,?,?,?,?,?);";

    public static String ACCOUNT_UPDATE_FIELD = "UPDATE PhantomAccount SET %field%=? WHERE UUID=?;";

    // PhantomAuth
    public static String AUTH_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS PhantomAuth (" +
            "UUID VARCHAR(36) PRIMARY KEY NOT NULL, Hash VARCHAR(64) NOT NULL);";

    public static String AUTH_GET_FROM_UUID = "SELECT Hash FROM PhantomAuth WHERE UUID=?;";

    public static String AUTH_INSERT_OR_UPDATE = "INSERT INTO PhantomAuth VALUES(?,?) ON DUPLICATE KEY UPDATE SET Hash=? WHERE UUID=?;";
}
