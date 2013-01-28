package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import org.openqa.selenium.By;

import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

/**
 * All selenium related step definitions with regards to element actions
 * 
 * @author joostschouten
 *
 */
@StepDefAnnotation
public class SeleniumElementSteps extends BaseStepDefinition{

	
	/*
	 * Given 
	 */
	
	/*
	 * When
	 */
	
	@When("^I drag the \"([^\"]*)\" onto the \"([^\"]*)\"$")
	public void I_drag_the_onto_the(String draggableLocator, String dragReceiverLocator) throws Throwable {
		seleniumElementService.waitForLoaders();
		seleniumElementService.dragElementTo(draggableLocator, dragReceiverLocator, 5000);
	}

	
	
	/*
	 * Then
	 */	
	

	/**
	 * @param element - the id of the element to check
	 * @param visibility - "visible" or "invisible"
	 */
	@Then("^the element \"(.*)\" is \"(.*)\"$")
	public void the_element_x_is_x(String elementLocator, String visibility) {
		
		By locator = seleniumElementService.translateLocator(elementLocator);
		
		boolean visible = true;
		if("invisible".equalsIgnoreCase(visibility)) {
			visible = false;
		}
		else if (!"visible".equalsIgnoreCase(visibility)) {
			throw new IllegalArgumentException("you can only check for visible or invisible elements");
		}
		
		seleniumElementService.isElementVisible(locator, visible);
	}
	
	@Then("^I should see the text \"([^\"]*)\"$")
	public void I_should_see_the_text(String expectedText) throws Throwable {
		seleniumElementService.findExpectedElement(By.xpath("//*[contains(text(),'" + expectedText + "')]"));
	}
}
