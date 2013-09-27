package com.gfk.senbot.framework.cucumber.stepdefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
		// TODO Auto-generated method stub
		
	}

	public By getLocator() {
		return locator;
	}


}
