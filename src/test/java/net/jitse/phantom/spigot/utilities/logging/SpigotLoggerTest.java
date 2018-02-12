package net.jitse.phantom.spigot.utilities.logging;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.util.Collections;

public class SpigotLoggerTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private SpigotLogger logger;
    private ConsoleCommandSender console;

    @Before
    public void setUp() {
        this.logger = new SpigotLogger("foo", LogLevel.INFO);
        this.console = Mockito.mock(ConsoleCommandSender.class);
        PowerMockito.mockStatic(Bukkit.class);
        PowerMockito.doReturn(Collections.singleton(PowerMockito.mock(Player.class))).when(Bukkit.getOnlinePlayers());
    }

    @Test
    public void testValid() {
        logger.log(LogLevel.FATAL, Mockito.anyString());
        Mockito.verify(console, Mockito.times(1)).sendMessage(Mockito.anyString());
    }

    @Test
    public void testInvalid() {
        logger.log(LogLevel.DEBUG, Mockito.anyString());
        Mockito.verify(console, Mockito.never()).sendMessage(Mockito.anyString());
    }
}