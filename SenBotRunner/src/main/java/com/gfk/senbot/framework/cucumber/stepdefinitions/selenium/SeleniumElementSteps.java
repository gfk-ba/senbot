package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;
import com.gfk.senbot.framework.services.selenium.ElementService;

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
public class SeleniumElementSteps extends BaseServiceHub {
	
	@Resource
	protected ElementService seleniumElementService;

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
	 * @param elementLocator - the id of the element to check
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
	
	@Then("^the text of element \"([^\"]*)\" on the \"([^\"]*)\" view should be \"([^\"]*)\"$")
	public void the_text_of_element_on_the_view_should_be(String elementIdentifier, String view, String expectedText) throws Throwable {
		WebElement elementFromReferencedView = seleniumElementService.getElementFromReferencedView(view, elementIdentifier);
		assertEquals("The element following element should match the expected text: " + seleniumElementService.getElementLocatorFromReferencedView(view, elementIdentifier), 
				getReferenceService().namespaceString(expectedText), elementFromReferencedView.getText());
	}
}
