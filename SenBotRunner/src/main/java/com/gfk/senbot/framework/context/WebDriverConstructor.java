package com.gfk.senbot.framework.context;

import org.openqa.selenium.WebDriver;

/**
 * An interface extracting the construction of the actual driver out of the test environment so that we can easily 
 * externalize the instantiation of the actual drivers and provide an easy way to enfore Just In Time creation.
 * 
 * @author joostschouten
 *
 */
public interface WebDriverConstructor {
	
	WebDriver construct(TestEnvironment testEnvironment);

}
