package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import static org.junit.Assert.fail;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.BeanUtils;

import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

@StepDefAnnotation
public class SeleniumPageRepresentationSteps extends BaseStepDefinition{
	
	@When("^I click \"([^\"]*)\" on the \"([^\"]*)\" view$")
	public void I_click_on_the(String elementName, String viewName) throws Throwable {
		seleniumElementService.getElementFromReferencedView(viewName, elementName).click();
	}
	
	/**
	 * A generic way to assert of a view/page contains a certain element. The element lookup is done though a naming convention. 
	 * variable_Name is matched up on argument "Variable name". So case insensitive and spaces are replaced by underscores
	 * 
	 * @param viewName
	 * @param elementName
	 * @throws Throwable
	 */
	@Then("^the \"([^\"]*)\" view should contain the element \"([^\"]*)\"$")
	public void the_view_should_contain(String viewName, String elementName) throws Throwable {
		WebElement found = seleniumElementService.getElementFromReferencedView(viewName, elementName);
		boolean fail = true;
		
		try{
			fail = found == null || !found.isDisplayed();			
		}
		catch (NoSuchElementException e) {
			//leave fail = true
		}
		if(fail) {			
			fail("The element \"" + elementName + "\" on view/page \"" + viewName + "\" is not found.");
		}
		
	}
	
	@Then("^the \"([^\"]*)\" view should not contain the element \"([^\"]*)\"$")
	public void the_view_should_not_contain_element(String viewName, String elementName) throws Throwable {
		WebElement found = seleniumElementService.getElementFromReferencedView(viewName, elementName);
		boolean fail = true;
		
		try{
			fail = found != null && found.isDisplayed();			
		}
		catch (NoSuchElementException e) {
			fail = false;
		}
		if(fail) {
			fail("The element \"" + elementName + "\" on view/page \"" + viewName + "\" is found where it is not expected.");
		}
	}	

}
