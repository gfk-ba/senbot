package com.gfk.senbot.framework.cucumber.stepdefinitions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;


public class ScenarioGlobalsTest {
	
	@Test
	public void testRegisterLoaderIndicators() {
		ScenarioGlobals globals = new ScenarioGlobals();
		By locator = By.id("blah");
		globals.registerLoaderIndicators(locator);
		
		assertNotNull(globals.getExpectedGlobalConditions());
		assertEquals("One condition should be founnd", 1, globals.getExpectedGlobalConditions().size());
		
		ExpectedGlobalCondition found = globals.getExpectedGlobalConditions().get(0);
		assertEquals(ExpectedWebElementCondition.class, found.getClass());
		ExpectedWebElementCondition foundImpl = (ExpectedWebElementCondition) found;
		assertEquals(locator, foundImpl.getLocator());
	}

}
