package net.jitse.api.storage;

import net.jitse.api.account.Account;
import net.jitse.api.exceptions.AccountFetchFailedException;

import java.util.UUID;
import java.util.function.BiConsumer;

interface AccountStorage {

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

    /**
     * Update a value permanently for a player's account.
     *
     * @param uuid  The UUID of the player.
     * @param field The account field you'd like to update.
     * @param value The new value for this account field.
     */
    void update(UUID uuid, AccountField field, Object value);

    /**
     * See {@link AccountStorage#update(UUID, AccountField, Object)}.
     */
    default void update(Account account, AccountField field, Object value) {
        update(account.getUniqueId(), field, value);
    }
}
