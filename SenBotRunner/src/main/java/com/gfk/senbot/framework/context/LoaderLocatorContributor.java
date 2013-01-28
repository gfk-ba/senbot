package com.gfk.senbot.framework.context;

import org.openqa.selenium.By;

import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarionCreationHook;

import cucumber.api.Scenario;

/**
 * The {@link ScenarionCreationHook} specific to the drive framework registering {@link Scenario} scoped
 * variables.
 * 
 * @author joostschouten
 *
 */
public class LoaderLocatorContributor implements ScenarionCreationHook {

    public static final By DRIVE_LOADER_INDICATOR      = By.id("gfkLogo");
    public static final By DRIVE_UI_DISABLER_INDICATOR = By.id("apacheLogo");
    public static final By DRIVE_UI_FETCHING_DATA      = By.id("wikipediaLogo");

    /**
     * Register the default loader locator so that the default selenium calls will always wait for this locator to be
     * invisible before proceeding
     */
    public void scenarionStarted(ScenarioGlobals scenarioGlobals) {
        scenarioGlobals.registerLoaderIndicators(DRIVE_LOADER_INDICATOR, DRIVE_UI_DISABLER_INDICATOR, DRIVE_UI_FETCHING_DATA);
    }

}
