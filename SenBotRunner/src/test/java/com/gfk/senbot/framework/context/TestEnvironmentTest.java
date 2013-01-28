package com.gfk.senbot.framework.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

public class TestEnvironmentTest {
	
	@After
	public void cleanup() {
		SenBotContext.cleanupSenBot();
	}

    @Test
    public void testEquals_allNull() {
        TestEnvironment left = new TestEnvironment(null, null, null);
        TestEnvironment right = new TestEnvironment(null, null, null);

        assertTrue(left.matches(right));
    }

    @Test
    public void testEquals_browser() {
        TestEnvironment left = new TestEnvironment(TestEnvironment.CH, null, null);
        TestEnvironment right = new TestEnvironment(TestEnvironment.CH, null, null);

        assertTrue(left.matches(right));

        right = new TestEnvironment(TestEnvironment.FF, null, null);
        assertFalse(left.matches(right));
    }

    @Test
    public void testEquals_browserVersion() {
        TestEnvironment left = new TestEnvironment(null, "LATEST", null);
        TestEnvironment right = new TestEnvironment(null, "LATEST", null);

        assertTrue(left.matches(right));

        right = new TestEnvironment(null, "IE8", null);
        assertFalse(left.matches(right));
    }

    @Test
    public void testEquals_OS() {
        TestEnvironment left = new TestEnvironment(null, null, Platform.WINDOWS);
        TestEnvironment right = new TestEnvironment(null, null, Platform.WINDOWS);

        assertTrue(left.matches(right));

        right = new TestEnvironment(null, null, Platform.LINUX);
        assertFalse(left.matches(right));

        right = new TestEnvironment(null, null, Platform.XP);
        assertTrue(left.matches(right));

        right = new TestEnvironment(null, null, Platform.VISTA);
        assertTrue(left.matches(right));

        right = new TestEnvironment(null, null, Platform.ANY);
        assertTrue(left.matches(right));
    }

    @Test
    public void testCleanupDriver() throws Throwable {
        final WebDriver webDriver = mock(WebDriver.class);
        TestEnvironment env = new TestEnvironment(null, null, Platform.WINDOWS){
            @Override
            public WebDriver getWebDriver(){
                return webDriver;
            }
        };

        assertEquals(webDriver, env.getWebDriver());

        env.cleanupDriver();

        verify(webDriver, times(1)).quit();
        env.cleanupAllDrivers();
    }        

    @Test
    public void testIsWebDriverAccessedSince() {
        TestEnvironment env = new TestEnvironment(TestEnvironment.FF, null, Platform.WINDOWS);
      
        long beforeAccess = System.currentTimeMillis() - 1;
        assertFalse(env.isWebDriverAccessedSince(0));
        assertFalse(env.isWebDriverAccessedSince(beforeAccess));

        env.getWebDriver();
        assertTrue(env.isWebDriverAccessedSince(0));
        assertTrue(env.isWebDriverAccessedSince(beforeAccess));
        assertFalse(env.isWebDriverAccessedSince(System.currentTimeMillis()));
        
        env.cleanupAllDrivers();
        
    }

}
