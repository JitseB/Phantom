package net.jitse.phantom.spigot.modules.auth.type;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class AuthTypeTest {

    @Test
    public void testLowercase() {
        AuthType none = AuthType.byString("none").orElse(null);
        assertNotNull(none);
        assertSame(none, AuthType.NONE);
    }

    @Test
    public void testUppercase() {
        AuthType none = AuthType.byString("NONE").orElse(null);
        assertNotNull(none);
        assertSame(none, AuthType.NONE);
    }
}