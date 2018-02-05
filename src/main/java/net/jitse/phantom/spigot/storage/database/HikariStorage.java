package net.jitse.phantom.spigot.storage.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.jitse.api.account.Account;
import net.jitse.api.account.rank.Rank;
import net.jitse.api.logging.Logger;
import net.jitse.api.storage.AccountField;
import net.jitse.api.storage.AccountStorage;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.account.PhantomAccount;
import net.jitse.phantom.spigot.exceptions.AccountFetchFailedException;
import net.jitse.phantom.spigot.util.MySqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HikariStorage implements AccountStorage {

    private final Phantom plugin;
    private final String host, user, password, database;
    private final int port;
    private final boolean ssl;

    private HikariDataSource dataSource;
    private boolean operational;

    public HikariStorage(Phantom plugin, String host, int port, String user, String password, String database, boolean ssl) {
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
        Logger.log(plugin, Logger.LogLevel.DEBUG, "Creating Hikari connection pool...");
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database + "?useSSL=" + ssl);
            config.setUsername(user);
            config.setPassword(password);
            dataSource = new HikariDataSource(config);
        } catch (Exception exception) {
            Logger.log(plugin, Logger.LogLevel.FATAL, "Could not create Hikari connection pool.");
            Logger.log(plugin, Logger.LogLevel.ERROR, exception.getMessage());
            return false;
        }
        Logger.log(plugin, Logger.LogLevel.INFO, "Created Hikari connection pool.");
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
                Logger.log(plugin, Logger.LogLevel.FATAL, "Could not create Hikari connection pool.");
                return false;
            }
        } catch (SQLException exception) {
            Logger.log(plugin, Logger.LogLevel.FATAL, "Could not create Hikari connection pool.");
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
    public void stopStorage() {
        dataSource.close();
        Logger.log(plugin, Logger.LogLevel.INFO, "Closed Hikari connection pool.");
    }

    @Override
    public Account getAccount(UUID uuid) throws AccountFetchFailedException {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null.");
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.SELECT_ACCOUNT_FROM_UUID);
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                // Return nothing.
                return null;
            }

            String name = resultSet.getString("Name");
            String rankName = resultSet.getString("Rank");
            Rank rank = plugin.getRanksManager().getRank(rankName).orElse(null);

            resultSet.close();
            statement.close();

            if (rank == null) {
                Logger.log(plugin, Logger.LogLevel.FATAL, name + " has a rank that doesn't exist! (Rank: " + rankName + ").");
                throw new AccountFetchFailedException("Your rank \"" + rankName + "\" doesn't exist in our system.");
            }

            return new PhantomAccount(uuid, name, rank);
        } catch (SQLException exception) {
            Logger.log(plugin, Logger.LogLevel.ERROR, exception.getMessage());
            throw new AccountFetchFailedException();
        }
    }

    @Override
    public boolean storeAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.INSERT_ACCOUNT);
            statement.setString(1, account.getUniqueId().toString());
            statement.setString(2, account.getName());
            statement.setString(3, account.getRank().getName());
            statement.setLong(4, System.currentTimeMillis());
            statement.setLong(5, System.currentTimeMillis());
            statement.setLong(6, 0);
            statement.execute();
            statement.close();
        } catch (SQLException exception) {
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
    public void update(UUID uuid, AccountField field, Object value) {
        if (uuid == null || field == null || value == null) {
            throw new IllegalArgumentException("UUID, Field or Value cannot be null.");
        }

        if (!field.canSet()) {
            throw new IllegalStateException("The specified field cannot be set.");
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.UPDATE_VALUE.replace("%field%", field.getSqlColumn()));
            statement.setObject(1, value);
            statement.setString(2, uuid.toString());
            statement.execute();
        } catch (SQLException exception) {
            Logger.log(plugin, Logger.LogLevel.ERROR, exception.getMessage());
        }
    }
}
