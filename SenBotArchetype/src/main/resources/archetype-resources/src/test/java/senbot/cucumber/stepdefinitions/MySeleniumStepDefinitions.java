#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.senbot.cucumber.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;
import com.gfk.senbot.framework.data.GenericUser;
import ${package}.senbot.cucumber.views.TestPage1;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.PendingException;
import cucumber.runtime.java.StepDefAnnotation;


@StepDefAnnotation
public class MySeleniumStepDefinitions extends BaseStepDefinition {
	
	/*
	 * Given
	 */
	
	@Given("^I am logged in as a ${symbol_escape}"(.*)${symbol_escape}" user${symbol_dollar}")
	public void I_am_logged_in_as_a_x_user(String userType) {
		GenericUser genericUser = getReferenceService().getUserForUserReference(userType);
		//TODO: login using the credentials of this referenced user

		throw new PendingException("Implement this step definition");
	}

	/*
	 * When
	 */

	@When("^I login as ${symbol_escape}"(.*)${symbol_escape}" with password ${symbol_escape}"(.*)${symbol_escape}"${symbol_dollar}")
	public void link_with_xpath_is_clicked(String userName, String password) {
		//TODO: login using these credentials

		throw new PendingException("Implement this step definition");
	}
	
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
	
	@Then("^I am logged in${symbol_dollar}")
	public void the_User_is_logged_in() {
		//TODO: assert you are logged in
		
		throw new PendingException("Implement this step definition");
	}
	
	@Then("^the System property \"([^\"]*)\" should be \"([^\"]*)\"$")
	public void the_System_property_should_be(String prop, String value) throws Throwable {
	    String foundProperty = System.getProperty(prop);
	    assertNotNull(foundProperty);
		assertEquals(value, foundProperty);
	}

}
