package com.gfk.senbot.framework.cucumber.stepdefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.context.SenBotContext;

public class ExpectedWebElementCondition implements ExpectedGlobalCondition {
	
	private static final Logger log = LoggerFactory.getLogger(ExpectedWebElementCondition.class);
	
	private final By locator;

	/**
	 * @param locator
	 */
	public ExpectedWebElementCondition(By locator) {
		this.locator = locator;
	}

	@Override
	public void checkExpected(WebDriver webDriver) {
		log.debug("Check for invisibility of " + locator);		
		ExpectedCondition<Boolean> invisibilityOfElementLocated = ExpectedConditions.invisibilityOfElementLocated(locator);
		new WebDriverWait(webDriver, SenBotContext.getSenBotContext().getSeleniumManager().getTimeout()).until(invisibilityOfElementLocated);
		log.debug("Invisibility of " + locator + " detected");
	}

	public By getLocator() {
		return locator;
	}
	
	@Override
	public String toString() {
		return "Wait for invisibilty of:" + locator;
	}


}
