package net.jitse.phantom.spigot.storage;

public interface Storage {

    /**
     * @return Whether the storage system was be set up correctly.
     */
    boolean enable();

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
    default void disable() {
    }
}
