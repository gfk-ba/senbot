package com.gfk.senbot.framework.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;


public class CucumberManagerTest {
	
	@Before
	public void setup() {
		SenBotContext.cleanupSenBot();
	}

	@Test
	public void testStartNewScenario() throws SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		CucumberManager manager = new CucumberManager("com.gfk.senbot.framework.context.MockScenarioCreationHook", null, false, 1);
		assertNull("No globals should be associated with the current thread when not yet started", manager.getCurrentScenarioGlobals());
		
		ScenarioGlobals instantiatedScenario = manager.startNewScenario();
		assertNotNull(instantiatedScenario);
		assertNotNull(instantiatedScenario.getAttribute(MockScenarioCreationHook.STARTED_ATTRIBUTE_KEY));
		assertEquals(MockScenarioCreationHook.STARTED_ATTRIBUTE_VALUE, instantiatedScenario.getAttribute(MockScenarioCreationHook.STARTED_ATTRIBUTE_KEY));
		assertEquals(instantiatedScenario, manager.getCurrentScenarioGlobals());
		
		manager.startNewScenario();
		assertNotSame(instantiatedScenario, manager.getCurrentScenarioGlobals());

		manager.stopNewScenario();
	}

	@Test
	public void testStopNewScenario() throws SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		CucumberManager manager = new CucumberManager("com.gfk.senbot.framework.context.MockScenarioCreationHook", null, false, 1);
		manager.startNewScenario();
		
		ScenarioGlobals stopNewScenario = manager.stopNewScenario();
		assertNotNull(stopNewScenario.getAttribute(MockScenarioCreationHook.STOPPED_ATTRIBUTE_KEY));
		assertEquals(stopNewScenario.getAttribute(MockScenarioCreationHook.STOPPED_ATTRIBUTE_KEY), MockScenarioCreationHook.STOPPED_ATTRIBUTE_VALUE);
		
		assertNull(manager.getCurrentScenarioGlobals());
	}


}
