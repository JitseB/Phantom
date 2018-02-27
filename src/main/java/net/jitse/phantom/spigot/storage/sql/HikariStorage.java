package net.jitse.phantom.spigot.storage.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.account.Account;
import net.jitse.phantom.spigot.account.AccountField;
import net.jitse.phantom.spigot.account.PhantomAccount;
import net.jitse.phantom.spigot.account.rank.Rank;
import net.jitse.phantom.spigot.exceptions.AccountFetchFailedException;
import net.jitse.phantom.spigot.exceptions.HashNotPresentException;
import net.jitse.phantom.spigot.storage.AuthStorage;
import net.jitse.phantom.spigot.utilities.logging.LogLevel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HikariStorage implements AuthStorage {

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
        plugin.getSpigotLogger().log(LogLevel.DEBUG, "Creating Hikari connection pool...");
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database + "?useSSL=" + ssl);
            config.setUsername(user);
            config.setPassword(password);
            dataSource = new HikariDataSource(config);
        } catch (Exception exception) {
            plugin.getSpigotLogger().log(LogLevel.FATAL, "Could not create Hikari connection pool.");
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
            return false;
        }
        plugin.getSpigotLogger().log(LogLevel.INFO, "Created Hikari connection pool " +
                "(" + user + "@" + host + ":" + port + ".");
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
                plugin.getSpigotLogger().log(LogLevel.FATAL, "Could not create Hikari connection pool.");
                return false;
            }
        } catch (SQLException exception) {
            plugin.getSpigotLogger().log(LogLevel.FATAL, "Could not create Hikari connection pool.");
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
            return false;
        }
        this.operational = true;
        return true;
    }

    @Override
    public boolean createPrerequisites() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.ACCOUNT_CREATE_TABLE);
            statement.execute();
            statement.close();
        } catch (SQLException exception) {
            plugin.getSpigotLogger().log(LogLevel.FATAL, "Could not create \"PhantomAccount\" table.");
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
            return false;
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.AUTH_CREATE_TABLE);
            statement.execute();
            statement.close();
        } catch (SQLException exception) {
            plugin.getSpigotLogger().log(LogLevel.FATAL, "Could not create \"PhantomAuth\" table.");
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void stopStorage() {
        dataSource.close();
        plugin.getSpigotLogger().log(LogLevel.INFO, "Closed Hikari connection pool.");
    }

    @Override
    public Account getAccount(UUID uuid) throws AccountFetchFailedException {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null.");
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.ACCOUNT_GET_FROM_UUID);
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                // Return nothing.
                return null;
            }

            String name = resultSet.getString("Name");
            String rankName = resultSet.getString("Rank");
            Rank rank = plugin.getRankManager().getRank(rankName).orElse(null);

            resultSet.close();
            statement.close();

            if (rank == null) {
                plugin.getSpigotLogger().log(LogLevel.FATAL, name + " has a rank that doesn't exist! (Rank: " + rankName + ").");
                throw new AccountFetchFailedException("Your rank \"" + rankName + "\" doesn't exist in our system.");
            }

            return new PhantomAccount(uuid, name, rank);
        } catch (SQLException exception) {
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
            throw new AccountFetchFailedException();
        }
    }

    @Override
    public boolean storeAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.ACCOUNT_INSERT_NEW);
            statement.setString(1, account.getUniqueId().toString());
            statement.setString(2, account.getName());
            statement.setString(3, account.getRank().getName());
            statement.setLong(4, System.currentTimeMillis());
            statement.setLong(5, System.currentTimeMillis());
            statement.setLong(6, 0);
            statement.execute();
            statement.close();
        } catch (SQLException exception) {
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
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
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.ACCOUNT_UPDATE_FIELD.replace("%field%", field.getSqlColumn()));
            statement.setObject(1, value);
            statement.setString(2, uuid.toString());
            statement.execute();
        } catch (SQLException exception) {
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
        }
    }

    @Override
    public String getHashedAuthenticator(UUID uuid) throws HashNotPresentException {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null.");
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.AUTH_GET_FROM_UUID);
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new HashNotPresentException();
            }

            String hash = resultSet.getString("Hash");

            resultSet.close();
            statement.close();

            return hash;
        } catch (SQLException exception) {
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
            throw new HashNotPresentException();
        }
    }

    @Override
    public boolean storeHash(UUID uuid, String hash) {
        if (uuid == null || hash == null) {
            throw new IllegalArgumentException("UUID or Hash cannot be null.");
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(MySqlQueries.AUTH_INSERT_OR_UPDATE);
            statement.setString(1, uuid.toString());
            statement.setString(2, hash);
            statement.setString(3, hash);
            statement.execute();
            statement.close();
        } catch (SQLException exception) {
            plugin.getSpigotLogger().log(LogLevel.ERROR, exception.getMessage());
            return false;
        }
        return true;
    }
}
