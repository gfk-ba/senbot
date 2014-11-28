package com.gfk.senbot.framework.services.selenium;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.context.TestEnvironment;

/**
 * 
 * The base class for the tests for all service classes
 * 
 * @author joostschouten
 *
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/cucumber.xml"})
public abstract class AbstractSenbotServiceTest {

    protected NavigationService      seleniumNavigationService;
    protected ElementService         seleniumElementService;
    protected FormService            seleniumFormService;
    protected SynchronisationService seleniumSynchronisationService;
    protected static TestEnvironment environment;
    protected WebDriver              driver;
    protected SeleniumManager        seleniumManager;
    
    @AfterClass
    public static void breakDownClass() {
    	SenBotContext.cleanupSenBot();
    }

    @Before
    public void setupBase() throws Exception {
    	environment = SenBotContext.getSenBotContext().getSeleniumManager().getSeleniumTestEnvironments().get(0);
    	SenBotContext.getSenBotContext().getSeleniumManager().associateTestEnvironment(environment);
        seleniumElementService = new ElementService();
        seleniumNavigationService = new NavigationService(seleniumElementService);
        seleniumFormService = new FormService(seleniumElementService);
        seleniumSynchronisationService = new SynchronisationService();
        driver = SenBotContext.getSenBotContext().getSeleniumDriver();
        seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();
    }

    @After
    public void breakDownBase() throws InterruptedException {
        SenBotContext senBotContext = SenBotContext.getSenBotContext();
        if(senBotContext != null) {        	
        	TestEnvironment deAssociatedTestEnvironment = senBotContext.getSeleniumManager().deAssociateTestEnvironment();
        }
    }

}
