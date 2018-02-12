package net.jitse.phantom.spigot.utilities.logging;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpigotLoggerTest {

    private final LogLevel max = LogLevel.INFO;

    private JavaPlugin plugin;
    private SpigotLogger logger;
    private ConsoleCommandSender console;

    @Before
    public void setUp() {
        JavaPlugin plugin = mock(JavaPlugin.class);
        when(plugin.getName()).thenReturn(anyString());
        this.logger = new SpigotLogger(plugin, max);
        this.console = mock(ConsoleCommandSender.class);
//        Player player = mock(Player.class);
//        Bukkit bukkit = mock(Bukkit.class);
//        Collection<? extends Player> collection = Collections.singleton(player);
//        //when(bukkit.getOnlinePlayers()).thenReturn(collection);
    }

    @Test
    public void testConsole() {
        logger.log(LogLevel.FATAL, anyString());
        verify(console, times(1)).sendMessage(anyString());
    }

    @Test
    public void testInvalid() {
        logger.log(LogLevel.DEBUG, anyString());
        verify(console, never()).sendMessage(anyString());
    }
}