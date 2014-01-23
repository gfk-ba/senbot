package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium.views;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


public class TablePage {

	@FindBy(how = How.XPATH, using = "//table")
	public WebElement table;

}
