package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;


import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;

import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

/**
 * All generic selenium cucumber step definitions with regards to managing html forms
 * 
 * @author joostschouten
 *
 */
@StepDefAnnotation
public class SeleniumFormSteps extends BaseStepDefinition{

	
	@When("^I fill the field \"(.*)\" with \"(.*)\"$")
	public void fill_the_field_with(String field, String value) {
		seleniumFormService.fillFormField_locator(field, value);
	}
	
	
}
