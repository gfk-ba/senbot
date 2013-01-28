package com.gfk.senbot.framework.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.cucumber.CucumberTestBase;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarionCreationHook;
import com.gfk.senbot.framework.data.ReferenceServicePopulator;

import cucumber.api.Scenario;

public class CucumberManager {
	
	private static Logger log = LoggerFactory.getLogger(CucumberManager.class);
	
	
	/**
	 * map the last when a scenario started so that the after scenario can do some checks on what the scenario
	 * has touched in its lifetime. This for example helps us check if a scenario uses Selenium or not
	 */
	private static Map<Thread, ScenarioGlobals> scenarioGlobalsMap = new HashMap<Thread, ScenarioGlobals>();
	private ScenarionCreationHook scenarioCreationHook;
	
	/**
	 * Constructor
	 * 
	 * @param scenarioGlobalsCreationHookClass the name (or null) in {@link String} format of the implementing {@link ScenarionCreationHook}
	 * class used to setup the {@link ScenarioGlobals} on each {@link Scenario} creation
	 * 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IllegalArgumentException 
	 */
	public CucumberManager(String scenarioGlobalsCreationHookClass) throws SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if(!StringUtils.isBlank(scenarioGlobalsCreationHookClass)) {
			Constructor<?> constructor = Class.forName(scenarioGlobalsCreationHookClass).getConstructor();
			scenarioCreationHook = (ScenarionCreationHook) constructor.newInstance();
		}
	}

	/**
	 * Called when a new scenario is started and associates a new {@link ScenarioGlobals} with the current thread so that
	 * they can be obtained again from within any process executed by the current {@link Thread}
	 *  
	 * @return
	 */
	public ScenarioGlobals startNewScenario() {
		
		ScenarioGlobals scenarioGlobals = new ScenarioGlobals();
		scenarioGlobalsMap.put(Thread.currentThread(), scenarioGlobals);
		if(scenarioCreationHook != null) {
			scenarioCreationHook.scenarionStarted(scenarioGlobals);
		}
		return scenarioGlobals;
	}
	
	/**
	 * @return the scenario globals
	 */
	public ScenarioGlobals getCurrentScenarioGlobals() {
		return scenarioGlobalsMap.get(Thread.currentThread());
	}

	/**
	 * Called at the end of an scenario?
	 * 
	 * @return
	 */
	public ScenarioGlobals stopNewScenario() {
		return scenarioGlobalsMap.remove(Thread.currentThread());
	}

}
