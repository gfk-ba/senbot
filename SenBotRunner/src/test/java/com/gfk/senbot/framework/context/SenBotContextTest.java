package com.gfk.senbot.framework.context;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.lf5.util.StreamUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class SenBotContextTest {

    private String alternateRuntimeResources;

	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        alternateRuntimeResources = System.getProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME);
    }

    @After
    public void breakDown() {
        SenBotContext.cleanupSenBot();
        if(StringUtils.isBlank(alternateRuntimeResources)) {        	
        	System.clearProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME);
        }
        else {
        	System.setProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME, alternateRuntimeResources);        	
        }
    }

    @AfterClass
    public static void breakDownClass() {
        SenBotContext.cleanupSenBot();
    }

    @Test
    public void testGetSenBotContextdefaultSpringCreation() throws URISyntaxException {
        SenBotContext senBotContext = SenBotContext.getSenBotContext();
        assertNotNull(senBotContext);
        assertNotNull(senBotContext.getSeleniumManager());
        assertNotNull(senBotContext.getRuntimeResources());
        
        URL classPathLocation = this.getClass().getClassLoader().getResource("");
        File classPathFile = new File(new URI(classPathLocation.toExternalForm()));
        String expectedLocation = classPathFile.getAbsolutePath();
        
        //if the tests are run using a SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME VM argument, expect that
        String vmRuntimeResourcesProp = System.getProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME);
		if(!StringUtils.isBlank(vmRuntimeResourcesProp)) {
			expectedLocation = vmRuntimeResourcesProp;
        }
        
        assertEquals(expectedLocation, senBotContext.getRuntimeResources());
    }

    @Test
    public void testGetRuntimeResourcesPopulatedFromProperties() {
    	//make sure to cleanup the senbot so the new system property is used
    	SenBotContext senBotContext = SenBotContext.getSenBotContext();
    	SenBotContext.cleanupSenBot();
        System.setProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME, "some/path");

        senBotContext = SenBotContext.getSenBotContext();
        assertNotNull(senBotContext.getRuntimeResources());
        assertEquals("some/path", senBotContext.getRuntimeResources());
        
        //make sure to cleanup the senbot so that this test does not interfere with other tests
        SenBotContext.cleanupSenBot();

    }

}
