package com.gfk.senbotdemo.cucumber.stepdefinitions;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;


import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;
import com.gfk.senbot.framework.data.GenericUser;
import com.gfk.senbotdemo.cucumber.views.TestPage1;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;


@StepDefAnnotation
public class MySeleniumStepDefinitions extends BaseStepDefinition {
	
	/*
	 * Given
	 */
	
	@Given("^I am logged in as a \"(.*)\" user$")
	public void I_am_logged_in_as_a_x_user(String userType) {
		GenericUser genericUser = getReferenceService().getUserForUserReference(userType);
		//TODO: login using the credentials of this referenced user

		throw new PendingException("Implement this step definition");
	}

	/*
	 * When
	 */

	@When("^I visit the pages:$")
	public void the_pages_have_been_visited(DataTable arguments) throws IOException {
		List<List<String>> asList = arguments.raw();
		for(List<String> row : asList) {
			seleniumNavigationService.navigate_to_url(row.get(0));
		}
	}
	
	@When("^I set the System property \"([^\"]*)\" to \"([^\"]*)\"$")
	public void I_set_the_System_property_to(String prop, String value) throws Throwable {
	    System.setProperty(prop, value);
	}
	
	@When("^I call a custom function in my view definition$")
	public void I_call_a_custom_funtion_in_my_view_defintion() throws IOException {
		SeleniumManager seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();
		TestPage1 viewRepresentation = seleniumManager.getViewRepresentation(TestPage1.class);		
		
		viewRepresentation.doSomeStuff();
	}
	
	/*
	 * Then
	 */
	
	@Then("^the System property \"([^\"]*)\" should be \"([^\"]*)\"$")
	public void the_System_property_should_be(String prop, String value) throws Throwable {
	    String foundProperty = System.getProperty(prop);
	    assertNotNull(foundProperty);
		assertEquals(value, foundProperty);
	}

}
