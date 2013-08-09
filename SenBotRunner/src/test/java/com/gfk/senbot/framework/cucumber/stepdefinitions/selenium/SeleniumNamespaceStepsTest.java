package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import javax.annotation.Resource;

import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;
import com.gfk.senbot.framework.data.SenBotReferenceService;
import com.gfk.senbot.framework.services.selenium.ElementService;

import cucumber.api.java.en.Then;

public class SeleniumNamespaceStepsTest extends BaseServiceHub {
	
	@Resource
    private ElementService seleniumElementService;

    @Then("^table cell \"([^\"]*)\" should have a namespaced value \"([^\"]*)\"$")
    public void table_cell_should_have_a_namespaced_value(String cellLocator, String expectedValue) throws Throwable {
        WebElement cell = seleniumElementService.translateLocatorToWebElement(cellLocator);
        
        String cellContent = cell.getText();
		String namespacenizedValue = getReferenceService().namespaceString(SenBotReferenceService.NAME_SPACE_PREFIX + expectedValue);
        assertFalse("The value should not start with the NS: prefix", namespacenizedValue.startsWith(SenBotReferenceService.NAME_SPACE_PREFIX));
        assertNotSame("The cell should not contain the non NS value", expectedValue, cellContent);
		assertEquals("The cell should equal the namespased value", namespacenizedValue, cellContent);
    }
    
    @Then("^table cell \"([^\"]*)\" should have a scenario namespaced value \"([^\"]*)\"$")
	public void table_cell_should_have_a_scenario_namespaced_value(String cellLocator, String expectedValue) throws Throwable {
    	WebElement cell = seleniumElementService.translateLocatorToWebElement(cellLocator);
    	
    	String cellContent = cell.getText();
    	String namespacenizedValue = getReferenceService().namespaceString(SenBotReferenceService.SCENARIO_NAME_SPACE_PREFIX + expectedValue);
    	assertFalse("The value should not start with the SNS: prefix", namespacenizedValue.startsWith(SenBotReferenceService.SCENARIO_NAME_SPACE_PREFIX));
    	assertNotSame("The cell should not contain the non SNS value", expectedValue, cellContent);
    	assertEquals("The cell should equal the namespased value", namespacenizedValue, cellContent);
	}
    
    @Then("^table cell \"([^\"]*)\" should not match \"([^\"]*)\"$")
    public void table_cell_should_not_match(String cellLocator, String exactValue) throws Throwable {
    	WebElement cell = seleniumElementService.translateLocatorToWebElement(cellLocator);
    	
    	assertNotSame(exactValue, cell.getText());
    }

}
