package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import javax.annotation.Resource;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.services.selenium.ElementService;
import com.gfk.senbot.framework.services.selenium.SynchronisationService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class WaitForLoadersStepsTest extends BaseServiceHub {
	
    public static final By GFK_LOADER       = By.id("gfkLogo");
    public static final By APACHE_LOADER    = By.id("apacheLogo");
    public static final By WIKIPEDIA_LOADER = By.id("wikipediaLogo");
    public static final By BUTTON           = By.id("button");
    
    @Resource
    private ElementService seleniumElementService;

    @Given("^in the source code the loader icons are assigned in the correct order$")
    public void in_the_source_code_the_loader_icons_are_assigned_in_the_correct_order() throws Throwable {
        ScenarioGlobals scenarioGlobals = getScenarioGlobals();
        scenarioGlobals.clearLoaderIndicators();
        scenarioGlobals.registerLoaderIndicators(GFK_LOADER, APACHE_LOADER, WIKIPEDIA_LOADER);
    }

    @When("^in the source code I wait for the loaders$")
    public void in_the_source_code_I_wait_for_the_loaders() throws Throwable {
        seleniumElementService.waitForLoaders();
    }

    @Then("^The button \"([^\"]*)\" is visible$")
    public void The_button_is_visible(String text) throws Throwable {
        SynchronisationService synchronisationService = new SynchronisationService();
        synchronisationService.checkForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='button'][contains(text()," + text + "')]")));
    }

    @Given("^in the source code the loader icons are assigned in the wrong order$")
    public void in_the_source_code_the_loader_icons_are_assigned_in_the_wrong_order() throws Throwable {
        ScenarioGlobals scenarioGlobals = getScenarioGlobals();
        scenarioGlobals.clearLoaderIndicators();
        scenarioGlobals.registerLoaderIndicators(APACHE_LOADER, GFK_LOADER, WIKIPEDIA_LOADER);
    }

}
