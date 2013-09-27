package com.gfk.senbot.framework.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.Platform;

import com.gfk.senbot.framework.services.selenium.ElementService;
import com.gfk.senbot.framework.services.selenium.NavigationService;

public class SeleniumManagerTest {

	private static final Logger log = Logger.getLogger(SeleniumManagerTest.class);
	
    /**
     * Is the timeout given to the constructor is test to a value, the vallue
     * has to be set in the object
     * 
     * @throws IOException
     * @throws AWTException 
     */
    @Test
    public void testSeleniumManager_timeoutSetting() throws IOException, AWTException {
        SeleniumManager localSeleniumManager = new SeleniumManager("http://www.gfk.com", "http://someHub", "FF,LATEST,WINDOWS", 1000, 800, 5);
        assertEquals(5, localSeleniumManager.getTimeout());
    }

    @Test
    public void testSeleniumManager_mousePosition() throws IOException, AWTException {
    	SeleniumManager localSeleniumManager = new SeleniumManager("http://www.gfk.com", "http://someHub", "FF,LATEST,WINDOWS", 1000, 800, 5);
    	
    	//only run the test if we are not in a headless process
    	try {
        	Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        	assertEquals(0, mouseLocation.x);
        	assertEquals(50, mouseLocation.y);
        }
        catch (HeadlessException he) {
        	//ignore as in a headless env we can never control the mouse
        }
    	catch(Throwable ex) {
    		log.warn("The mouse position test failed but this could very well be due to a multi-screen setup", ex);
    	}
    }

    @Test
    public void testSenBotContext_domainProtocolAdditionWhenMissing() throws IOException, AWTException {
    	SeleniumManager seleniumManager = new SeleniumManager("www.gfk.com", "", "FF,LATEST,WINDOWS", 1000, 800, 5);
    	
    	assertEquals("http://www.gfk.com", seleniumManager.getDefaultDomain());
    }

    @Test
    public void testSenBotContext_hubProperlySet() throws IOException, AWTException {
        String expectedHub = "http://some_hub";
        SeleniumManager manager = new SeleniumManager("http://www.gfk.com", expectedHub, "FF,LATEST,WINDOWS", 1000, 800, 5);
        assertEquals(new URL(expectedHub), manager.getSeleniumHub());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSenBotContext_missingSeleniumTestTarget() throws IOException, AWTException {
        new SeleniumManager("http://www.gfk.com", "http://someHub", "", 1000, 800, 5);
    }

    @Test
    public void testSenBotContext_shortenedTargetEnvironment() throws IOException, AWTException {
        SeleniumManager seleniumManager = new SeleniumManager("http://www.gfk.com", "http://someHub", "FF,LATEST;CH", 1000, 800, 5);
        
        List<TestEnvironment> seleniumTestEnvironments = seleniumManager.getSeleniumTestEnvironments();
        assertEquals(2, seleniumTestEnvironments.size());
        assertEquals("FF", seleniumTestEnvironments.get(0).getBrowser());
        assertEquals("LATEST", seleniumTestEnvironments.get(0).getBrowserVersion());
        assertEquals(Platform.ANY, seleniumTestEnvironments.get(0).getOS());

        assertEquals("CH", seleniumTestEnvironments.get(1).getBrowser());
        assertEquals("ANY", seleniumTestEnvironments.get(1).getBrowserVersion());
        assertEquals(Platform.ANY, seleniumTestEnvironments.get(1).getOS());
    }

    @Test(expected = MalformedURLException.class)
    public void testSenBotContext_mallformattedHubUrl() throws IOException, AWTException {
        new SeleniumManager("http://www.gfk.com", "I'm an invalid URL", "FF,LATEST,WINDOWS", 1000, 800, 5);
    }

    @Test
    public void testSenBotContext_blankImplicitWait() throws IOException {
    	SeleniumManager seleniumManager = new SeleniumManager("http://www.gfk.com", "http://someHub", "FF,LATEST,WINDOWS", 1000, 800, 5, "");
    	assertNull(seleniumManager.getImplicitTimeout());
    }

    @Test
    public void testSenBotContext_implicitWait() throws IOException {
    	SeleniumManager seleniumManager = new SeleniumManager("http://www.gfk.com", "http://someHub", "FF,LATEST,WINDOWS", 1000, 800, 5, "4");
    	assertEquals(new Integer(4), seleniumManager.getImplicitTimeout());
    }

    @Test
    public void testSenBotContext_seleniumTestEnvironmentTargetCreation() throws IOException, AWTException {
        SeleniumManager seleniumManager = new SeleniumManager("http://www.gfk.com", "http://someHub", "FF,LATEST,ANY;CH,LATEST,ANY", 1000, 800, 5);

        assertFalse(seleniumManager.getSeleniumTestEnvironments().isEmpty());
        assertEquals("FF", seleniumManager.getSeleniumTestEnvironments().get(0).getBrowser());
        assertEquals("LATEST", seleniumManager.getSeleniumTestEnvironments().get(0).getBrowserVersion());
        assertEquals("ANY", seleniumManager.getSeleniumTestEnvironments().get(0).getOS().name());
        assertNotNull(seleniumManager.getSeleniumTestEnvironments().get(0).getWebDriver());

        assertEquals("CH", seleniumManager.getSeleniumTestEnvironments().get(1).getBrowser());
        assertEquals("LATEST", seleniumManager.getSeleniumTestEnvironments().get(1).getBrowserVersion());
        assertEquals("ANY", seleniumManager.getSeleniumTestEnvironments().get(1).getOS().name());
        assertNotNull(seleniumManager.getSeleniumTestEnvironments().get(1).getWebDriver());

        seleniumManager.cleanUp();

        assertTrue(seleniumManager.getSeleniumTestEnvironments().isEmpty());
    }

    @Test
    public void testGetWebDriver_firstCall() {
        assertNull(SenBotContext.getSeleniumDriver());
    }

	@Test
	public void testGetViewRepresentation() throws Exception {
		SenBotContext senBotContext = SenBotContext.getSenBotContext();
		SeleniumManager seleniumManager = senBotContext.getSeleniumManager();
		seleniumManager.associateTestEnvironment(seleniumManager.getSeleniumTestEnvironments().get(0));
		senBotContext.getCucumberManager().startNewScenario();
		
		
		NavigationService service = new NavigationService(new ElementService());
		service.navigate_to_url("resource_location:/test_pages/exampleTable.html");
		
		MockExampleTablePage examplePage = seleniumManager.getViewRepresentation(MockExampleTablePage.class);
		
		assertNotNull("ensure that the table row has indeed been matched up with the associated web element", examplePage.table_Row_1);
		
		MockExampleTablePage secondCall = seleniumManager.getViewRepresentation(MockExampleTablePage.class);
		assertEquals(examplePage, secondCall);

		MockExampleTablePage forcedUpdate = seleniumManager.getViewRepresentation(MockExampleTablePage.class, true);
		assertNotSame(examplePage, forcedUpdate);
		
		senBotContext.cleanupSenBot();
	}
    
}
