package net.jitse.phantom.spigot.storage.redis;

import net.jitse.api.account.Account;
import net.jitse.api.storage.AccountField;
import net.jitse.api.storage.AccountStorage;
import net.jitse.phantom.spigot.logging.SpigotLogger;
import net.jitse.phantom.spigot.Phantom;
import net.jitse.phantom.spigot.account.PhantomAccount;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;

public class RedisStorage implements AccountStorage {

    private final Phantom plugin;
    private final String host, password;
    private final int port;
    private final boolean ssl;

    private JedisPool pool;
    private boolean operational;

    public RedisStorage(Phantom plugin, String host, int port, String password, boolean ssl) {
        this.plugin = plugin;
        this.host = host;
        this.port = port;
        this.password = password;
        this.ssl = ssl;

        this.operational = false;
    }

    @Override
    public boolean createStorage() {
        SpigotLogger.log(plugin, SpigotLogger.LogLevel.DEBUG, "Creating Redis connection pool...");
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            this.pool = new JedisPool(config, host, port, 10000, password, ssl);
        } catch (Exception exception) {
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.FATAL, "Could not create Redis connection pool.");
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, exception.getMessage());
            return false;
        }

        SpigotLogger.log(plugin, SpigotLogger.LogLevel.INFO, "Created Redis connection pool.");
        return true;
    }

    @Override
    public boolean testStorage() {
        try (Jedis connection = pool.getResource()) {
            connection.ping();
        } catch (Exception exception) {
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.FATAL, "Could not create Redis connection pool.");
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, exception.getMessage());
            return false;
        }
        this.operational = true;
        return true;
    }

    @Override
    public void stopStorage() {
        pool.close();
        SpigotLogger.log(plugin, SpigotLogger.LogLevel.INFO, "Closed Redis connection pool.");
    }

    @Override
    public Account getAccount(UUID uuid) {
        try (Jedis connection = pool.getResource()) {
            String json = connection.get(uuid.toString());

            if (json == null || json.isEmpty()) {
                return null;
            }

            SpigotLogger.log(plugin, SpigotLogger.LogLevel.DEBUG, "Successfully grabbed account from Redis server.");
            return plugin.getGson().fromJson(json, PhantomAccount.class);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public boolean storeAccount(Account account) {
        try (Jedis connection = pool.getResource()) {
            String json = plugin.getGson().toJson(account);
            connection.set(account.getUniqueId().toString(), json);
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.DEBUG, "Successfully stored " + account.getName() + "'s account in the Redis server.");
        } catch (Exception exception) {
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
        throw new IllegalStateException("Cannot update Redis account through UUID, need to " +
                "use \"RedisStorage#update(Account account, AccountField field, Object value)\".");
    }

    @Override
    public void update(Account account, AccountField field, Object value) {
        if (account == null || field == null || value == null) {
            throw new IllegalArgumentException("Account, Field or Value cannot be null.");
        }

        if (!field.canSet()) {
            throw new IllegalStateException("The specified field cannot be set.");
        }

        try (Jedis connection = pool.getResource()) {
            boolean exists = connection.get(account.getUniqueId().toString()) != null;

            if (exists) {
                connection.del(account.getUniqueId().toString());
            }

            String json = plugin.getGson().toJson(account);
            connection.set(account.getUniqueId().toString(), json);
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.DEBUG, "Successfully updated " + account.getName() + "'s account in the Redis server.");
        } catch (Exception exception) {
            SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, exception.getMessage());
        }
    }
}
