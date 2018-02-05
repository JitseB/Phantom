package net.jitse.api.exceptions;

public class AccountFetchFailedException extends Exception {

    private final String message;

    public AccountFetchFailedException() {
        this("");
    }

    public AccountFetchFailedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public boolean hasMessage() {
        return !message.isEmpty();
    }
}
