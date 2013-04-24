package com.gfk.senbot.framework.context;

import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarionCreationShutdownHook;

public class MockScenarioCreationHook implements ScenarionCreationShutdownHook {

	public static final String STARTED_ATTRIBUTE_KEY = "STARTED_ATTRIBUTE_KEY";
	public static final String STARTED_ATTRIBUTE_VALUE = "STARTED_ATTRIBUTE_VALUE";

	public static final String STOPPED_ATTRIBUTE_KEY = "STOPPED_ATTRIBUTE_KEY";
	public static final String STOPPED_ATTRIBUTE_VALUE = "STOPPED_ATTRIBUTE_VALUE";
	
	public void scenarionStarted(ScenarioGlobals scenarioGlobals) {
		scenarioGlobals.setAttribute(STARTED_ATTRIBUTE_KEY, STARTED_ATTRIBUTE_VALUE);
	}

	@Override
	public void scenarionShutdown(ScenarioGlobals scenarioGlobals) {
		scenarioGlobals.setAttribute(STOPPED_ATTRIBUTE_KEY, STOPPED_ATTRIBUTE_VALUE);
	}

}
