package com.gfk.senbot.framework.services.selenium;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gfk.senbot.framework.context.SenBotContext;

public class NavigationServiceMockedEnvTest extends AbstractSenbotServiceTest {
	
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
	   
    @Test
    public void testNavigate_to_url_onUnixPath() throws Exception {
        //Cleanup the senbot so it will be initialiyed with the new property
        SenBotContext.cleanupSenBot();
        System.setProperty("senbotContext.alternateRuntimeResources", "/some/path");
        final WebDriver mockDriver = Mockito.mock(WebDriver.class);
        
        //setup spring again after it has been cleared
    	ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:/cucumber.xml"});
    	classPathXmlApplicationContext.getBean(SenBotContext.class);
        
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
