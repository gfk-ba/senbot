package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import cucumber.api.java.en.Given;

/**
 * 
 * @author gdschu
 *
 */
public class Synchronisation {

	/**
	 * 
	 * @param waitInSeconds
	 * @throws Throwable
	 * @deprecated This should never be part of a test unless it truely is part if the spec of your story. Will be removed in a later version
	 */
    @Given("^I wait for \"([^\"]*)\" seconds$")
    public void I_wait_for_x_seconds(String waitInSeconds) throws Throwable {
        Thread.sleep(Long.valueOf(waitInSeconds) * 1000);
    }

}
