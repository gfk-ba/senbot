package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import javax.annotation.Resource;

import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.services.selenium.ElementService;
import com.gfk.senbot.framework.services.selenium.ExpectedTableDefinition;
import com.gfk.senbot.framework.services.selenium.TableService;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.StepDefAnnotation;

/**
 * All HTML  table related step definitions
 * 
 * @author joostschouten
 *
 */
@StepDefAnnotation
public class SeleniumTableSteps extends BaseServiceHub {
	
	@Resource
	protected ElementService seleniumElementService;

	@Resource
	protected TableService seleniumTableService;
	
	/*
	 * Then
	 */
	
	@Then("^the table \"(.*)\" should match$")
	public void the_table_x_should_match(String tableDescriptor, DataTable expectedContent) throws Throwable {
		WebElement table = seleniumElementService.translateLocatorToWebElement(tableDescriptor);
		
		ExpectedTableDefinition expected = new ExpectedTableDefinition(expectedContent);
		seleniumTableService.compareTable(expected, table);
	}
	

	@Then("^the table \"(.*)\" should contain the columns$")
	public void the_table_x_should_contain_columns(String tableDescriptor, DataTable expectedContent) throws Throwable {
		WebElement table = seleniumElementService.translateLocatorToWebElement(tableDescriptor);
		
		ExpectedTableDefinition expected = new ExpectedTableDefinition(expectedContent);
		expected.setMatchOnlyPassedInColumns(true);
		
		seleniumTableService.compareTable(expected, table);
	}

}
