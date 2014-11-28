package com.gfk.senbot.framework.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/cucumber.xml"})
public class CucumberManagerTest {
	
	@After
	public void setup() {
		SenBotContext.cleanupSenBot();
	}

	@Test
	public void testStartNewScenario() throws SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
		CucumberManager manager = new CucumberManager("com.gfk.senbot.framework.context.MockScenarioCreationHook", null, false, 1, 3600);
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
		CucumberManager manager = new CucumberManager("com.gfk.senbot.framework.context.MockScenarioCreationHook", null, false, 1, 3600);
		manager.startNewScenario();
		
		ScenarioGlobals stopNewScenario = manager.stopNewScenario();
		assertNotNull(stopNewScenario.getAttribute(MockScenarioCreationHook.STOPPED_ATTRIBUTE_KEY));
		assertEquals(stopNewScenario.getAttribute(MockScenarioCreationHook.STOPPED_ATTRIBUTE_KEY), MockScenarioCreationHook.STOPPED_ATTRIBUTE_VALUE);
		
		assertNull(manager.getCurrentScenarioGlobals());
	}


}
