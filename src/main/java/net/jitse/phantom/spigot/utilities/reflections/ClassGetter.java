package net.jitse.phantom.spigot.utilities.reflections;

import org.bukkit.Bukkit;

public class ClassGetter {

    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage()
            .getName().replace(".", ",").split(",")[3];

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + SERVER_VERSION + "." + name);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Class<?> getNMSArrayClass(String name) {
        try {
            return Class.forName("[Lnet.minecraft.server." + SERVER_VERSION + "." + name + ";");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Class<?> getOBCClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + "." + name);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
