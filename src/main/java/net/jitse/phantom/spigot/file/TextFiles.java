package net.jitse.phantom.spigot.file;

import net.jitse.phantom.logging.SpigotLogger;
import net.jitse.phantom.spigot.Phantom;

import java.io.*;

public class TextFiles {

    private final Phantom plugin;

    public TextFiles(Phantom plugin) {
        this.plugin = plugin;
    }

    public void create() {
        File thankYou = new File(plugin.getDataFolder(), "thankyou.txt");
        File copyright = new File(plugin.getDataFolder(), "copyright.txt");

        // Thank you file.
        if (!thankYou.exists()) {
            try {
                thankYou.getParentFile().mkdirs();
                thankYou.createNewFile();

                try {
                    InputStream initialStream = plugin.getResource("thankyou.txt");
                    byte[] buffer = new byte[initialStream.available()];
                    initialStream.read(buffer);
                    OutputStream outStream = new FileOutputStream(thankYou);
                    outStream.write(buffer);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } catch (IOException exception) {
                SpigotLogger.log(plugin, SpigotLogger.LogLevel.WARN, "Could not create thank you file.");
                SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, exception.getMessage());
            }
        }


        // Copyright file.
        if (!copyright.exists()) {
            try {
                copyright.getParentFile().mkdirs();
                copyright.createNewFile();

                try {
                    InputStream initialStream = plugin.getResource("copyright.txt");
                    byte[] buffer = new byte[initialStream.available()];
                    initialStream.read(buffer);
                    OutputStream outStream = new FileOutputStream(copyright);
                    outStream.write(buffer);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } catch (IOException exception) {
                SpigotLogger.log(plugin, SpigotLogger.LogLevel.WARN, "Could not create copyright file.");
                SpigotLogger.log(plugin, SpigotLogger.LogLevel.ERROR, exception.getMessage());
            }
        }
    }
}