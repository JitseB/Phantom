package net.jitse.phantom.spigot.storage;

import net.jitse.phantom.spigot.exceptions.HashNotPresentException;

import java.util.UUID;
import java.util.function.BiConsumer;

public interface AuthStorage extends AccountStorage {

    /**
     * Should be ran async at any time!
     *
     * @param uuid The UUID of the player.
     * @return The hashed passphrase or PIN code.
     * @throws HashNotPresentException When account does not have an existing hash in the database.
     */
    String getHashedAuthenticator(UUID uuid) throws HashNotPresentException;

    /**
     * Returns value in callback on async thread.
     *
     * @param uuid     The UUID of the player.
     * @param callback A tuple with the hash and exception (if thrown).
     */
    default void getHashedAuthenticator(UUID uuid, BiConsumer<String, HashNotPresentException> callback) {
        new Thread(() -> {
            try {
                String hash = getHashedAuthenticator(uuid);
                callback.accept(hash, null);
            } catch (HashNotPresentException exception) {
                callback.accept(null, exception);
            }
        }).start();
    }


    /**
     * Store the hash of a player so they can be asked to authenticate next time they join.
     *
     * @param uuid The UUID of the player.
     * @param hash The hash of the passphrase or PIN code.
     * @return Whether the action was successful.
     */
    boolean storeHash(UUID uuid, String hash);
}
