package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium.views;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.gfk.senbot.framework.context.SenBotContext;


public class TestPage1 {

	@FindBy(how = How.ID, using = "navigation")
	public WebElement navigation;

	@FindBy(how = How.XPATH, using = "//a[@id='hide_navigation_link']")
	public WebElement hide_navigation_link;

	@FindBy(how = How.ID, using = "testPage2")
	public WebElement link2;

	public void doSomeStuff() {
		link2.click();
		
		assertTrue(SenBotContext.getSeleniumDriver().getCurrentUrl().endsWith("cucumber_test_page2.html"));
	}

}
