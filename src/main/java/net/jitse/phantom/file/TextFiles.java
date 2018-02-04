package net.jitse.phantom.file;

import net.jitse.api.logging.Logger;
import net.jitse.phantom.Phantom;

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
                Logger.log(plugin, Logger.LogLevel.WARN, "Could not create thank you file.");
                Logger.log(plugin, Logger.LogLevel.ERROR, exception.getMessage());
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
                Logger.log(plugin, Logger.LogLevel.WARN, "Could not create copyright file.");
                Logger.log(plugin, Logger.LogLevel.ERROR, exception.getMessage());
            }
        }
    }
}