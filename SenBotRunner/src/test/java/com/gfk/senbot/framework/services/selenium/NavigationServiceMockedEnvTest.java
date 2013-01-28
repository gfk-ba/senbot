package com.gfk.senbot.framework.services.selenium;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;

import com.gfk.senbot.framework.context.SenBotContext;

public class NavigationServiceMockedEnvTest  extends AbstractSenbotServiceTest{
	
	private String alternateRuntimeResources;

	@Before
    public void setup() {
        alternateRuntimeResources = System.getProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME);
    }

    @After
    public void breakDown() {
        if(StringUtils.isBlank(alternateRuntimeResources)) {        	
        	System.clearProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME);
        }
        else {
        	System.setProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME, alternateRuntimeResources);        	
        }
    }
	
	@AfterClass
	public static void cleanup() {
		//make sure to cleanup the senbot so that this test does not interfere with other tests
        System.clearProperty("senbotContext.alternateRuntimeResources");
        SenBotContext.cleanupSenBot();
	}

    @Test
    public void testNavigate_to_url_onUnixPath() throws Exception {
        //Cleanup the senbot so it will be initialiyed with the new property
        SenBotContext.cleanupSenBot();
        System.setProperty("senbotContext.alternateRuntimeResources", "/some/path");
        final WebDriver mockDriver = Mockito.mock(WebDriver.class);
        
        String pageUnderTest = "/test_pages/exampleButton.html";
        String expectedUrl = "file:///some/path" + pageUnderTest;

        NavigationService serviceUnderTest = new NavigationService(seleniumElementService) {
            
            @Override
            public WebDriver getWebDriver() {
                return mockDriver;
            }
        };
        
        serviceUnderTest.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + pageUnderTest);
        Mockito.verify(mockDriver, Mockito.times(1)).get(expectedUrl);
        
        SenBotContext.cleanupSenBot();
    }



}
