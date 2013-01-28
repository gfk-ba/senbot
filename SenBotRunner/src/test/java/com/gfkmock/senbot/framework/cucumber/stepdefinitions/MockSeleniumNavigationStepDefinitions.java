package com.gfkmock.senbot.framework.cucumber.stepdefinitions;

import org.openqa.selenium.WebDriver;

import com.gfk.senbot.framework.cucumber.stepdefinitions.selenium.SeleniumNavigationSteps;

/**
 * This class is placed outside the com.gfk package so that cucumber will not
 * pick it up in it's tests
 * 
 * @author joostschouten
 * 
 */
public class MockSeleniumNavigationStepDefinitions extends SeleniumNavigationSteps {

	private WebDriver mockDriver;

	public MockSeleniumNavigationStepDefinitions(WebDriver mockDriver) {
		super();
		this.mockDriver = mockDriver;
	}

	@Override
	public WebDriver getWebDriver() {
		return mockDriver;
	}

}
