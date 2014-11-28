package com.gfk.senbot.framework.cucumber.stepdefinitions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/cucumber.xml"})
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
