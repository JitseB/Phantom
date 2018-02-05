package net.jitse.api.storage;

import net.jitse.api.account.Account;
import net.jitse.phantom.spigot.exceptions.AccountFetchFailedException;

import java.util.UUID;
import java.util.function.BiConsumer;

public interface Storage {

    /**
     * @return Whether the storage system could be set up correctly.
     */
    boolean createStorage();

    /**
     * Should normally be ran async.
     *
     * @return Whether the storage system actually works correctly.
     */
    boolean testStorage();

    /**
     * Create the prerequisites of the table (after testing).
     * These tasks contain e.g. the creation of MySQL tables.
     *
     * @return Whether the action went successful.
     */
    default boolean createPrerequisites() {
        return true;
    }

    /**
     * @return Whether the storage system is fully operational.
     */
    boolean isOperational();

    /**
     * Disengage the storage system (if required).
     */
    default void stopStorage() {
    }

    /**
     * Should be ran async at any time!
     *
     * @param uuid The UUID of the player.
     * @return The account of the provided UUID.
     * @throws AccountFetchFailedException if fetch of account failed.
     */
    Account getAccount(UUID uuid) throws AccountFetchFailedException;

    /**
     * Returns value in callback on async thread.
     *
     * @param uuid     The UUID of the player.
     * @param callback A tuple with the account and exception (if thrown).
     */
    default void getAccount(UUID uuid, BiConsumer<Account, AccountFetchFailedException> callback) {
        new Thread(() -> {
            try {
                Account account = getAccount(uuid);
                callback.accept(account, null);
            } catch (AccountFetchFailedException exception) {
                callback.accept(null, exception);
            }
        }).start();
    }

    /**
     * Internal method to store the account in the storage system.
     *
     * @param account The account to be stored.
     * @return Whether the action was successful.
     */
    boolean storeAccount(Account account);

    void update(UUID uuid, AccountField field, Object value);

    default void update(Account account, AccountField field, Object value) {
        update(account.getUniqueId(), field, value);
    }
}
