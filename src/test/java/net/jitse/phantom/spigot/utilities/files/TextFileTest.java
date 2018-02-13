package net.jitse.phantom.spigot.utilities.files;

import org.bukkit.plugin.java.JavaPlugin;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TextFileTest {

    private static JavaPlugin plugin;

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void setUpClass() {
        plugin = mock(JavaPlugin.class);
        given(plugin.getDataFolder()).willReturn(folder.getRoot());
    }

    @Test
    public void testThankYouFile() throws Exception {
        File mock = new File(getClass().getResource("/thankyou.txt").getPath());
        when(plugin.getResource("thankyou.txt")).thenReturn(new FileInputStream(mock));

        TextFile textFile = new TextFile(plugin, "thankyou");
        assertTrue(textFile.createIfNotExists());
    }

    @Test
    public void testCopyrightFile() throws Exception {
        File mock = new File(getClass().getResource("/copyright.txt").getPath());
        when(plugin.getResource("copyright.txt")).thenReturn(new FileInputStream(mock));

        TextFile textFile = new TextFile(plugin, "copyright");
        assertTrue(textFile.createIfNotExists());
    }
}