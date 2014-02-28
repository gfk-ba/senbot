package com.gfk.senbot.framework.cucumber.stepdefinitions;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.internal.runners.statements.Fail;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.cucumber.stepdefinitions.selenium.views.TestPage1;
import com.gfk.senbot.framework.services.selenium.ElementService;
import com.gfk.senbot.framework.services.selenium.NavigationService;

import cucumber.api.DataTable;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

@StepDefAnnotation
public class CucumberTestFixture {
	
	@Resource
	private NavigationService seleniumNavigationService;

	@Resource
	private ElementService seleniumElementService;

	@When("^I visit the pages:$")
	public void the_pages_have_been_visited(DataTable arguments) throws IOException {
		List<List<String>> asList = arguments.raw();
		for(List<String> row : asList) {
			seleniumNavigationService.navigate_to_url(row.get(0));
		}
	}

	@When("^I call a custom function in my view definition$")
	public void I_call_a_custom_funtion_in_my_view_defintion() throws IOException {
		SeleniumManager seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();
		TestPage1 viewRepresentation = seleniumManager.getViewRepresentation(TestPage1.class);	
		
		viewRepresentation.doSomeStuff();
	}
	
}
