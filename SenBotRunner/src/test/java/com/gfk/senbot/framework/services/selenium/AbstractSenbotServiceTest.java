package com.gfk.senbot.framework.services.selenium;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;

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
public abstract class AbstractSenbotServiceTest {

    protected NavigationService      seleniumNavigationService;
    protected ElementService         seleniumElementService;
    protected FormService            seleniumFormService;
    protected SynchronisationService seleniumSynchronisationService;
    protected static SenBotContext   senBotContext;
    protected static TestEnvironment environment;
    protected WebDriver              driver;
    protected SeleniumManager        seleniumManager;

    @BeforeClass
    public static void setupSelenium() throws Exception {
        senBotContext = SenBotContext.getSenBotContext();
        environment = senBotContext.getSeleniumManager().getSeleniumTestEnvironments().get(0);
    }

    @AfterClass
    public static void breakDownClass() throws InterruptedException {
        SenBotContext.cleanupSenBot();
    }

    @Before
    public void setupBase() throws Exception {
        senBotContext.getSeleniumManager().associateTestEnvironment(environment);
        seleniumElementService = new ElementService();
        seleniumNavigationService = new NavigationService(seleniumElementService);
        seleniumFormService = new FormService(seleniumElementService);
        seleniumSynchronisationService = new SynchronisationService();
        driver = senBotContext.getSeleniumDriver();
        seleniumManager = senBotContext.getSeleniumManager();
    }

    @After
    public void breakDownBase() throws InterruptedException {
        TestEnvironment deAssociatedTestEnvironment = senBotContext.getSeleniumManager().deAssociateTestEnvironment();
    }

}
