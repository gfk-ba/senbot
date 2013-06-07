#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.senbot;

import org.openqa.selenium.By;

import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarionCreationShutdownHook;

import cucumber.api.Scenario;

/**
 * The {@link ScenarionCreationHook} specific to the drive framework registering {@link Scenario} scoped
 * variables.
 */
public class ScenarioCreationHook implements ScenarionCreationShutdownHook {

	public static final By LOADER_INDICATOR = By.id("yourLoaderId");
	public static final By UI_DISABLER_INDICATOR = By.id("yourSecondLoaderId");
	
	/**
	 * Register the default loader locator so that the default selenium calls will always wait for this locator to be
	 * invisible before proceeding
	 */
	public void scenarionStarted(ScenarioGlobals scenarioGlobals) {
		scenarioGlobals.registerLoaderIndicators(LOADER_INDICATOR, UI_DISABLER_INDICATOR);
	}
	
	public void scenarionShutdown(ScenarioGlobals scenarioGlobals) {
		//defaults to doing nothing. You could for example cleanup some data setup done in the scenario
	}

}
