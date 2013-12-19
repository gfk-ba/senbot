package com.gfk.senbot.framework.cucumber.stepdefinitions;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.context.CucumberManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.context.TestEnvironment;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.StepDefAnnotation;

/**
 * A {@link StepDefAnnotation} for altering the generated report based on senario outcomes.
 * 
 * @author joostschouten
 *
 */
@StepDefAnnotation
public class CucumberReportingExtension extends BaseServiceHub {

    private static Logger log = LoggerFactory.getLogger(CucumberManager.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        log.debug("Scenarion started");
        ScenarioGlobals startNewScenario = getCucumberManager().startNewScenario();
    }

    /**
     * Capture a selenium screenshot when a test fails and add it to the Cucumber generated report
     * if the scenario has accessed selenium in its lifetime
     * @throws InterruptedException 
     */
    @After
    public void afterScenario(Scenario scenario) throws InterruptedException {
        log.debug("Scenarion finished");
        ScenarioGlobals scenarioGlobals = getCucumberManager().getCurrentScenarioGlobals();
    	TestEnvironment testNev = getSeleniumManager().getAssociatedTestEnvironment();
    	if (testNev != null && scenarioGlobals != null) {
    		boolean scenarioUsedSelenium = testNev.isWebDriverAccessedSince(scenarioGlobals.getScenarioStart());
    		if (scenarioUsedSelenium) {
    			if (scenario.isFailed()) {
    				log.debug("Scenarion failed while using selenium, so capture screenshot");
    				TakesScreenshot seleniumDriver = (TakesScreenshot) SenBotContext.getSeleniumDriver();
					byte[] screenshotAs = seleniumDriver.getScreenshotAs(OutputType.BYTES);
					scenario.embed(screenshotAs, "image/png");
    			}
    		}
    	}
        getCucumberManager().stopNewScenario();
    }

}
