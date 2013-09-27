package com.gfk.senbot.framework.cucumber.stepdefinitions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.context.TestEnvironment;

import cucumber.api.Scenario;

/**
 * A thin wrapper class managing some global variables relating to the currently executing Scenario.
 * 
 * This will allow you to setup some data at a Scenario level and maintaining within the scenario scope. For example you could
 * say something like:
 * Given I'm using the authentication scope username:password
 * When I interact with my API in one way
 * When I interact with my API in another way
 * Then my API should return
 * 
 * The Scenario globals will help you maintain the authentication scope across all three API interaction steps and allow you 
 * to keep you steps clean of this scope info
 * 
 * 
 * @author joostschouten
 *
 */
public class ScenarioGlobals {

    private Map<String, Object> scenarioAttributes = new HashMap<String, Object>();
    private final Long          scenarioStart;
    private TestEnvironment     testEnvironment;
    private WebDriver           driver             = SenBotContext.getSeleniumDriver();
    private String 				namespace		   = null;
	private List<ExpectedGlobalCondition> expectedGlobalConditions = new ArrayList<ExpectedGlobalCondition>();

    /**
     * Constructor
     */
    public ScenarioGlobals() {
        scenarioStart = System.currentTimeMillis();
    }

    /**
     * A map containing all scenario attributes
     * 
     * @param key The key
     * @param value The value
     */
    public void setAttribute(String key, Object value) {
        scenarioAttributes.put(key, value);
    }

    /**
     * @return A map containing all scenario attributes
     */
    public Object getAttribute(String key) {
        return scenarioAttributes.get(key);
    }

    /**
     * Stores the time of creation of this {@link ScenarioGlobals} object so that we can use it what services
     * are used during it's lifetime. For example used for checking if selenium is used or not.
     * 
     * @return scenarioStart
     */
    public Long getScenarioStart() {
        return scenarioStart;
    }

    /**
     * Register loader with the {@link ScenarioGlobals} so the different Selenium services can wait for them to be removed
     * @param loaderIndicators
     */
    public void registerLoaderIndicators(By... loaderIndicators) {
    	for(By locator : loaderIndicators) {
    		this.expectedGlobalConditions.add(new ExpectedWebElementCondition(locator));
    	}
    }

    /**
     * Clear loader with the {@link ScenarioGlobals} so the different Selenium services can wait for them to be removed
     */
    public void clearExpectedGlobalConditions() {
        this.expectedGlobalConditions.clear();
    }

    /**
     * @return The {@link TestEnvironment} this {@link Scenario} is running in
     */
    public TestEnvironment getTestEnvironment() {
        return testEnvironment;
    }

    /**
     * @param The {@link TestEnvironment} this {@link Scenario} is running in
     */
    public void setTestEnvironment(TestEnvironment testEnvironment) {
        this.testEnvironment = testEnvironment;
    }

    /**
     * @return a unique to this Scenario namespace string 
     */
	public String getNameSpace() {
		if(namespace == null) {
			namespace = "SNS" + new Integer(UUID.randomUUID().hashCode()).toString() + "-";
		}
		return namespace;
	}

	public List<ExpectedGlobalCondition> getExpectedGlobalConditions() {
		return expectedGlobalConditions;
	}

	public void addExpectedGlobalConditions(
			ExpectedGlobalCondition expectedGlobalCondition) {
		expectedGlobalConditions.add(expectedGlobalCondition);
		
	}

}
