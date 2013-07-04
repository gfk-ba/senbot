package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.BeanUtils;

import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

@StepDefAnnotation
public class SeleniumPageRepresentationSteps extends BaseStepDefinition{
	
	@When("^I click \"([^\"]*)\" on the \"([^\"]*)\" view$")
	public void I_click_on_the(String elementName, String viewName) throws Throwable {
		
		long startOfLookup = System.currentTimeMillis();
		int timeoutInMillies = getSeleniumManager().getTimeout() * 1000;
		boolean pass = false;
		
		while(!pass) {
			try{
				clickViewElement(elementName, viewName);
				pass = true;
			}
			catch (WebDriverException ex) {
				//catch an exception as it might indicate the click is caught by another element which has yet to fade away. Swallow untill timeout is exceeded
				if(System.currentTimeMillis() - startOfLookup > timeoutInMillies) {
					throw ex;
				}
			}

			if(!pass) {				
				Thread.sleep(200);
			}
		}
		
	}
	
	private void clickViewElement(String elementName, String viewName) throws Throwable {
		WebElement found = the_view_should_contain(viewName, elementName);
		try{			
			found.click();
		}
		catch (NoSuchElementException nsee) {
			fail("The element \"" + elementName + "\" on view \"" + viewName + "\" is not found");
		}
	}
	
	/**
	 * A generic way to assert of a view/page contains a certain element. The element lookup is done though a naming convention. 
	 * variable_Name is matched up on argument "Variable name". So case insensitive and spaces are replaced by underscores
	 * 
	 * @param viewName
	 * @param elementName
	 * @throws Throwable
	 */
	@Then("^the \"([^\"]*)\" view should show the element \"([^\"]*)\"$")
	public WebElement the_view_should_show(String viewName, String elementName) throws Throwable {
		return seleniumElementService.viewShouldShowElement(viewName, elementName);
		
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
	public WebElement the_view_should_contain(String viewName, String elementName) throws Throwable {
		return seleniumElementService.viewShouldContainElement(viewName, elementName);
		
	}
	
	@Then("^the \"([^\"]*)\" view should not show the element \"([^\"]*)\"$")
	public void the_view_should_not_contain_element(String viewName, String elementName) throws Throwable {
		
		seleniumElementService.viewShouldNotShowElement(viewName, elementName);
	}

	
	
}
