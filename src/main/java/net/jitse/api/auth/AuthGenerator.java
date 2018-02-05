package net.jitse.api.auth;

import java.util.Random;

public class AuthGenerator {

    private final Random random;
    private final String[] first, second;

    public AuthGenerator() {
        this.random = new Random();
        this.first = new String[]{"Pig", "Cow", "Cauldron", "Spigot", "Mine", "Craft", "Biome", "Creeper", "Sandbox"};
        this.second = new String[]{"Creative", "Nether", "Mojang", "Bukkit", "Steve", "Skin", "Pickaxe", "Server"};
    }

    public String generatePhrase() {
        return first[random.nextInt(first.length)] + second[random.nextInt(second.length)] + generatePIN();
    }

    public String generatePIN() {
        return String.valueOf(random.nextInt(9)) + String.valueOf(random.nextInt(9)) +
                String.valueOf(random.nextInt(9)) + String.valueOf(random.nextInt(9));
    }
}
