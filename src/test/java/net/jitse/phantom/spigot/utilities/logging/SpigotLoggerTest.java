package net.jitse.phantom.spigot.utilities.logging;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.Collections;
import java.util.logging.Logger;

@PrepareForTest({Bukkit.class, SpigotLogger.class})
public class SpigotLoggerTest {

    private final Logger logger = Logger.getLogger("SpigotLoggerTest");
    private final LogLevel max = LogLevel.INFO;

    private SpigotLogger spigotLogger;
    private ConsoleCommandSender console;

    @Before
    public void setUp() {
        logger.info("Testing the SpigotLogger class.");
        this.spigotLogger = new SpigotLogger("foo", max);

        this.console = Mockito.mock(ConsoleCommandSender.class);

//        PluginManager pm = new ListingPluginManager();

        PowerMockito.mockStatic(Bukkit.class);
//        Server server = PowerMockito.mock(Server.class)
//        PowerMockito.doReturn(pm).when(server).getPluginManager();
//        Mockito.when(Bukkit.getServer()).thenReturn(server);

        PowerMockito.doReturn(Collections.singleton(Mockito.mock(Player.class))).when(Bukkit.getOnlinePlayers());
    }

    @Test
    public void testValid() {
        spigotLogger.log(LogLevel.FATAL, Mockito.anyString());
        Mockito.verify(console, Mockito.times(1)).sendMessage(Mockito.anyString());
    }

    @Test
    public void testInvalid() {
        spigotLogger.log(LogLevel.DEBUG, Mockito.anyString());
        Mockito.verify(console, Mockito.never()).sendMessage(Mockito.anyString());
    }
}