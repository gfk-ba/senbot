package com.gfk.senbot.framework.cucumber;

import cucumber.api.java.en.Given;

/**
 * 
 * @author gdschu
 *
 */
public class Synchronisation {

    /*
     * Given
     */

    @Given("^I wait for \"([^\"]*)\" seconds$")
    public void I_wait_for_x_seconds(String waitInSeconds) throws Throwable {
        Thread.sleep(Long.valueOf(waitInSeconds) * 1000);
    }

}
