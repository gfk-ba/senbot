package com.gfk.senbot.framework.context;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/cucumber.xml"})
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
    	SenBotContext.cleanupSenBot();
        System.setProperty(SenBotContext.SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME, "some/path");

        SenBotContext senBotContext = SenBotContext.getSenBotContext();
        assertNotNull(senBotContext.getRuntimeResources());
        assertEquals("some/path", senBotContext.getRuntimeResources());

    }

}
