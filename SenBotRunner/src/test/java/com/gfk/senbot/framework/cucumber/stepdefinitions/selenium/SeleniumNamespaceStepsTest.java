package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import static org.junit.Assert.*;

import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;
import com.gfk.senbot.framework.data.SenBotReferenceService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SeleniumNamespaceStepsTest extends BaseStepDefinition {

    @Then("^table cell \"([^\"]*)\" should have a namespaced value \"([^\"]*)\"$")
    public void table_cell_should_have_a_namespaced_value(String cellLocator, String expectedValue) throws Throwable {
        WebElement cell = seleniumElementService.translateLocatorToWebElement(cellLocator);
        
        String cellContent = cell.getText();
		String namespacenizedValue = getReferenceService().namespacenizeString(SenBotReferenceService.NAME_SPACE_PREFIX + expectedValue);
        assertFalse("The value should not start with the NS: prefix", namespacenizedValue.startsWith(SenBotReferenceService.NAME_SPACE_PREFIX));
        assertNotSame("The cell should not contain the non NS value", expectedValue, cellContent);
		assertEquals("The cell should equal the namespased value", namespacenizedValue, cellContent);
    }

}
