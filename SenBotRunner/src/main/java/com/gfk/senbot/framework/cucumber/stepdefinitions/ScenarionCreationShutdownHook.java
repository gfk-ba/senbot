package com.gfk.senbot.framework.cucumber.stepdefinitions;

/**
 * If you want to do something at scenario start, you implement this interface
 * 
 * @author joostschouten
 *
 */
public interface ScenarionCreationShutdownHook {
	
	void scenarionStarted(ScenarioGlobals scenarioGlobals);

	void scenarionShutdown(ScenarioGlobals scenarioGlobals);
	
}
