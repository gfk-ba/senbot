package com.gfk.senbot.framework.cucumber.stepdefinitions;

import java.io.IOException;
import java.util.List;

import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.services.selenium.NavigationService;

import cucumber.api.DataTable;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

@StepDefAnnotation
public class CucumberTestFixture {
	
	/**
	 * TODO: autowire this 
	 */
	private NavigationService seleniumNavigationService = SenBotContext.getBean(NavigationService.class);

	@When("^I visit the pages:$")
	public void the_pages_have_been_visited(DataTable arguments) throws IOException {
		List<List<String>> asList = arguments.raw();
		for(List<String> row : asList) {
			seleniumNavigationService.navigate_to_url(row.get(0));
		}
	}
	
}
