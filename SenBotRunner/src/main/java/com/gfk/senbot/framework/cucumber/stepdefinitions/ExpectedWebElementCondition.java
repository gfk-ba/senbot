package com.gfk.senbot.framework.cucumber.stepdefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gfk.senbot.framework.context.SenBotContext;

public class ExpectedWebElementCondition implements ExpectedGlobalCondition {
	
	private final By locator;

	/**
	 * 
	 * @param locator
	 */
	public ExpectedWebElementCondition(By locator) {
		this.locator = locator;
	}

	@Override
	public void checkExpected(WebDriver webDriver) {
		ExpectedCondition<Boolean> invisibilityOfElementLocated = ExpectedConditions.invisibilityOfElementLocated(locator);
		new WebDriverWait(webDriver, SenBotContext.getSenBotContext().getSeleniumManager().getTimeout()).until(invisibilityOfElementLocated);
	}

	public By getLocator() {
		return locator;
	}


}
