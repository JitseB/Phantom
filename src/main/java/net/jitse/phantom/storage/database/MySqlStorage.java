package net.jitse.phantom.storage.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import net.jitse.api.account.Account;
import net.jitse.api.logging.Logger;
import net.jitse.api.storage.AccountField;
import net.jitse.api.storage.Storage;
import net.jitse.phantom.Phantom;
import net.jitse.phantom.util.MySqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlStorage implements Storage {

    private final Phantom plugin;
    private final String host, user, password, database;
    private final int port;
    private final boolean ssl;

    private MysqlDataSource dataSource;
    private boolean operational;

    public MySqlStorage(Phantom plugin, String host, int port, String user, String password, String database, boolean ssl) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
        this.ssl = ssl;

        this.operational = false;
    }

    @Override
    public boolean createStorage() {
        Logger.log(plugin, Logger.LogLevel.INFO, "Creating MySql connection pool...");
        this.dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setPort(port);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setDatabaseName(database);
        dataSource.setUseSSL(ssl);
        Logger.log(plugin, Logger.LogLevel.INFO, "Created MySql connection pool.");
        return true;
    }

    @Override
    public boolean testStorage() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT 1+1 AS 'result';");
            ResultSet resultSet = statement.executeQuery();

            boolean success = resultSet.next() && resultSet.getInt("result") == 2;

            resultSet.close();
            statement.close();

            if (!success) {
                Logger.log(plugin, Logger.LogLevel.FATAL, "Could not create MySql connection pool.");
                return false;
            }
        } catch (SQLException exception) {
            Logger.log(plugin, Logger.LogLevel.FATAL, "Could not create MySql connection pool.");
            Logger.log(plugin, Logger.LogLevel.ERROR, exception.getMessage());
            return false;
        }
        this.operational = true;
        return true;
    }

    @Override
    public boolean createPrerequisites() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.CREATE_PHANTOM_ACCOUNTS_TABLE);
            statement.execute();
            statement.close();
        } catch (SQLException exception) {
            Logger.log(plugin, Logger.LogLevel.FATAL, "Could not create \"PhantomAccounts\" table.");
            Logger.log(plugin, Logger.LogLevel.ERROR, exception.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean isOperational() {
        return operational;
    }

    @Override
    public Account getAccount(UUID uuid) {
        return null;
    }

    @Override
    public boolean storeAccount(Account account) {
        return false;
    }

    @Override
    public void update(UUID uuid, AccountField field, Object value) {

    }
}
