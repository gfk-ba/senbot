package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.openqa.selenium.By;

import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.cucumber.stepdefinitions.BaseStepDefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

/**
 * All selenium Cucumber step definitions controlling selenium navigation
 * 
 * @author joostschouten
 */
@StepDefAnnotation
public class SeleniumNavigationSteps extends BaseStepDefinition {

    /****************************************************************
     * Given
     ****************************************************************/
    @Given("I am on the homepage")
    public void I_am_on_the_homepage() throws IOException {
        navigate_to_webpage(getSeleniumManager().getDefaultDomain());
    }

    @Given("I am looking at the homepage")
    @Deprecated
    public void I_am_looking_at_the_homepage() throws IOException {
        navigate_to_webpage(getSeleniumManager().getDefaultDomain());
    }

    @Given("I am on webpage \"(.*)\"")
    public void I_am_on_webpage_x(String url) throws IOException {
        navigate_to_webpage(url);
    }

    private void navigate_to_webpage(String url) throws IOException {
        seleniumNavigationService.navigate_to_url(url);
    }

    @Given("^I am on the \"(.*)\" page$")
    public void I_am_on_the_x_page(String pageName) throws IOException {
        String url = getReferenceService().getUrlForPageReference(pageName);
        seleniumNavigationService.navigate_to_url(getSeleniumManager().getDefaultDomain() + url);
    }

    /****************************************************************
     * When
     ****************************************************************/

    @When("I navigate to the homepage")
    public void I_navigate_to_the_homepage() throws IOException {
        navigate_to_webpage(getSeleniumManager().getDefaultDomain());
    }

    @When("^I click on the link with id \"(.*)\"$")
    public void I_click_on_the_link_with_id(String linkId) {
        seleniumElementService.findExpectedElement(By.id(linkId)).click();
    }

    /**
     * 
     * @param attributeName
     * @param attributeValue
     * 
     * @deprecated This is a bit of a hack to make targeting links based on title text etc. We should actually have proper css classes and dom id's
     * to target the DOM elements
     */
    @When("^I click the link with \"(.*)\" \"(.*)\"$")
    public void I_click_the_link_with(String attributeName, String attributeValue) {
        seleniumNavigationService.clickElementWithAttributeValue(attributeName, attributeValue);
    }

    /**
     * Click a button with the passed text on it
     * @param xpathLink
     */
    @When("^I click the \"(.*)\" button")
    public void I_click_the_x_button(String textOnButton) {
        seleniumNavigationService.clickButtonWithText(textOnButton);
    }

    @When("^I refresh the page$")
    public void pageRefresh() {
        getWebDriver().navigate().refresh();
    }

    /****************************************************************
     * Then
     ****************************************************************/
    @Then("I am shown the homepage")
    public void I_am_shown_the_homepage() throws IOException {
        seleniumNavigationService.isCurrentlyOnPage(getSeleniumManager().getDefaultDomain());
    }

    @Then("^the page title is \"(.*)\"$")
    public void the_page_title_is_x(String expectedPageTitle) {
        assertEquals(expectedPageTitle, SenBotContext.getSeleniumDriver().getTitle());
    }

    @Then("^the \"(.*)\" page is shown$")
    public void the_x_page_is_shown(String pageName) throws IOException {
        String url = getReferenceService().getUrlForPageReference(pageName);
        seleniumNavigationService.isCurrentlyOnPage(SenBotContext.getSenBotContext().getSeleniumManager().getDefaultDomain() + url);
    }

}