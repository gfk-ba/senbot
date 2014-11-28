package com.gfk.senbot.framework;

import org.openqa.selenium.WebDriver;

import com.gfk.senbot.framework.context.CucumberManager;
import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.context.TestEnvironment;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.data.SenBotReferenceService;

/**
 * A base class used by services and cucumber step definitions to have quick access to all SenBot services
 * 
 * @author joostschouten
 *
 */
public abstract class BaseServiceHub {
	
	public BaseServiceHub() {
	}
	
	/**
	 * 
	 * @return the {@link SenBotContext} singleton
	 */
	public SenBotContext getSenBotContext() {
		return SenBotContext.getSenBotContext();
	}

	/**
	 *
	 * @return the {@link WebDriver} for the currently active {@link TestEnvironment}
	 */
	public WebDriver getWebDriver() {
		return getSenBotContext().getSeleniumDriver();
	}

	public SeleniumManager getSeleniumManager() {
		return getSenBotContext().getSeleniumManager();
	}

	public SenBotReferenceService getReferenceService() {
		return getSenBotContext().getReferenceService();
	}

	public CucumberManager getCucumberManager() {
		return getSenBotContext().getCucumberManager();
	}
	
	public ScenarioGlobals getScenarioGlobals() {
		return getCucumberManager().getCurrentScenarioGlobals();
	}
	

}
