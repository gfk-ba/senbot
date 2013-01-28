package com.gfk.senbot.framework.cucumber.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.json.JSONObject;

import cc.plural.jsonij.JPath;
import cc.plural.jsonij.Value;

import com.gfk.senbot.framework.data.GenericUser;
import com.gfk.senbot.framework.services.APIAccessService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.StepDefAnnotation;

@StepDefAnnotation
public class WebServiceAccessSteps extends BaseStepDefinition {
	
	@Given("^I access the API though the \"(.*)\"$")
	public void I_access_the_API_though_the_browser(String apiAccessMode) throws Throwable {
		getScenarioGlobals().setAttribute(APIAccessService.API_ACCESS_MODE_KEY, apiAccessMode);
	}

	@Given("^I authenticate my API requests as a \"(.*)\" user$")
	public void I_authenticate_my_API_requests_as_a(String userReference) throws Throwable {
		GenericUser authenticationUser = getReferenceService().getUserForUserReference(userReference);
		getScenarioGlobals().setAttribute(APIAccessService.API_AUTHENTICATION_USER, authenticationUser);
	}

	@Then("^the response should contain \"(.*)\" \"(.*)\"$")
	public void the_response_should_contain(String parameterName, String expectedParameterValue) throws Throwable {
		apiAccessService.assertCorrectResonse(apiAccessService.getLastJSONResponse());
		
		JSONObject response = apiAccessService.getLastJSONResponse();
		if(!expectedParameterValue.startsWith("/")) {
			parameterName = "/" + parameterName;
		}
		
		Value foundValue = JPath.evaluate(response.toString(), parameterName);
		
		if(foundValue == null) {
			fail("The expected parameter \"" + parameterName + "\" is not available in the response: \n\n" + apiAccessService.getLastJSONResponse().toString(4));
		}
		assertEquals("The expected parameter " + parameterName + " should match", expectedParameterValue, foundValue.toString());
	}

	@Then("^I should get HTTP response code \"([^\"]*)\"$")
	public void I_should_get_HTTP_response_code(int expectedResponseCode) throws Throwable {
	    assertEquals("The http response should match", new Integer(expectedResponseCode), apiAccessService.getLastHTTPResponseCode());
	}

}
