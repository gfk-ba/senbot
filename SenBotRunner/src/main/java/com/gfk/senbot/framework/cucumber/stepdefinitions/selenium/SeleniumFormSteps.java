package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;


import javax.annotation.Resource;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;
import com.gfk.senbot.framework.services.selenium.ElementService;
import com.gfk.senbot.framework.services.selenium.FormService;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

/**
 * All generic selenium cucumber step definitions with regards to managing html forms
 * 
 * @author joostschouten
 *
 */
@StepDefAnnotation
public class SeleniumFormSteps extends BaseServiceHub {
	
	@Resource
	protected FormService seleniumFormService;

	
	@When("^I fill the field \"[^\"]*\" with \"[^\"]*\"$")
	public void fill_the_field_with(String field, String value) {
		seleniumFormService.fillFormField_locator(field, value);
	}

	@When("^I fill the \"[^\"]*\" field on view \"[^\"]*\" with \"[^\"]*\"$")
	public void fill_the_field_with(String field, String view, String value) throws IllegalArgumentException, IllegalAccessException {
		seleniumFormService.fillFormField_fromView(view, field, value);
	}

	@Then("^the field \"[^\"]*\" on view \"[^\"]*\" is set to \"[^\"]*\"$")
	public void the_field_x_on_view_y_is_set_to_z(String field, String view, String value) throws IllegalArgumentException, IllegalAccessException {
		seleniumFormService.isFormFieldOnViewSetTo(view, field, value);
	}
	
	@When("^I set the \"([^\"]*)\" select on the \"([^\"]*)\" view to option \"([^\"]*)\"$")
	public void I_set_the_select_on_the_view_to_option(String selectElementName, String viewName, String optionText) throws Throwable {
		seleniumFormService.setSelectOptionOnView(viewName, selectElementName, optionText);
	}

	@When("^I check the \"([^\"]*)\" checkbox on the \"([^\"]*)\" view$")
	public void I_check_the_x_checkbox_on_the_y_view(String checkboxRef, String view) throws Throwable {
		seleniumFormService.checkCheckboxOnView(view, checkboxRef);
	}

	@When("^I uncheck the \"([^\"]*)\" checkbox on the \"([^\"]*)\" view$")
	public void I_uncheck_the_x_checkbox_on_the_y_view(String checkboxRef, String view) throws Throwable {
		seleniumFormService.uncheckCheckboxOnView(view, checkboxRef);
	}

	@Then("^option \"([^\"]*)\" of \"([^\"]*)\" select on the \"([^\"]*)\" should be selected$")
	public void option_x_of_y_select_on_the_view_z_should_be_selected(String optionText, String selectElementName, String viewName) throws Throwable {
		seleniumFormService.isOptionOfSelectForViewSelected(viewName, selectElementName, optionText);
	}
	
	
}
