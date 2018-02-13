package net.jitse.phantom.spigot.utilities.files;

import net.jitse.phantom.spigot.Phantom;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TextFileTest {

    @Mock
    private static JavaPlugin plugin;

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void beforeClass() {
        plugin = mock(Phantom.class);
        given(plugin.getDataFolder()).willReturn(folder.getRoot());
    }

    @Before
    public void setUp() throws Exception {
        mockResource("thankyou.txt");
    }

    @Test
    public void test() throws Exception {
        TextFile textFile = new TextFile(plugin, "thankyou");
        assertTrue(textFile.createIfNotExists());
    }

    private void mockResource(String file) throws Exception {
        File mock = new File(getClass().getResource("/" + file).getPath());
        when(plugin.getResource(file)).thenReturn(new FileInputStream(mock));
    }
}