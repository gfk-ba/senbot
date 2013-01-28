package com.gfk.senbot.framework.context;

import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarionCreationHook;

public class MockScenarioCreationHook implements ScenarionCreationHook {

	public static final String ATTRIBUTE_KEY = "ATTRIBUTE_KEY";
	public static final String ATTRIBUTE_VALUE = "ATTRIBUTE_VALUE";
	
	public void scenarionStarted(ScenarioGlobals scenarioGlobals) {
		scenarioGlobals.setAttribute(ATTRIBUTE_KEY, ATTRIBUTE_VALUE);
	}

}
