package net.jitse.api.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthValidator {

    private final MessageDigest messageDigest;

    public AuthValidator() throws NoSuchAlgorithmException {
        this.messageDigest = MessageDigest.getInstance("SHA-256");
    }

    public String hash(String input) {
        messageDigest.update(input.getBytes());
        return new String(messageDigest.digest());
    }

    public boolean validate(String hash, String input) {
        return hash(input).equals(hash);
    }
}
