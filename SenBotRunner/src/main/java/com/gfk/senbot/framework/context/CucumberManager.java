package com.gfk.senbot.framework.context;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.cucumber.CucumberReportGenerator;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarionCreationShutdownHook;

import cucumber.api.Scenario;

public class CucumberManager {
	
	private static Logger log = LoggerFactory.getLogger(CucumberManager.class);
	
	/**
	 * map the last when a scenario started so that the after scenario can do some checks on what the scenario
	 * has touched in its lifetime. This for example helps us check if a scenario uses Selenium or not
	 */
	private static Map<Thread, ScenarioGlobals> scenarioGlobalsMap = new HashMap<Thread, ScenarioGlobals>();
	private ScenarionCreationShutdownHook scenarioCreationHook;

	/**
	 * Should the resulting report be opened in the browser after completion of the suite
	 */
	private boolean openResultingReportAfterCompletion;

	private static Thread shutDownHook;
	
	/**
	 * Constructor
	 * 
	 * @param scenarioGlobalsCreationHookClass the name (or null) in {@link String} format of the implementing {@link ScenarionCreationShutdownHook}
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
	public CucumberManager(String scenarioGlobalsCreationHookClass, 
			String defaultCucumberOptionsString,
			boolean openResultingReportAfterCompletion) throws SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		
		this.setOpenResultingReportAfterCompletion(openResultingReportAfterCompletion);
		if(System.getProperty("cucumber.options") == null) {		
			String overwrite = null;
			if(System.getProperty("features") != null) {
				//allow for a shorthand for the cucumber.options
				overwrite = System.getProperty("features");
			}
			else if(!StringUtils.isBlank(defaultCucumberOptionsString)) {
				overwrite = defaultCucumberOptionsString;
			}
			if(overwrite != null) {
				System.setProperty("cucumber.options", overwrite);				
			}
		}
		
		
		if(!StringUtils.isBlank(scenarioGlobalsCreationHookClass)) {
			Constructor<?> constructor = Class.forName(scenarioGlobalsCreationHookClass).getConstructor();
			scenarioCreationHook = (ScenarionCreationShutdownHook) constructor.newInstance();
		}
	}

	/**
	 * Called when a new scenario is started and associates a new {@link ScenarioGlobals} with the current thread so that
	 * they can be obtained again from within any process executed by the current {@link Thread}
	 *  
	 * @return {@link ScenarioGlobals}
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
	 * @return {@link ScenarioGlobals}
	 */
	public ScenarioGlobals stopNewScenario() {
		if(scenarioCreationHook != null) {
			scenarioCreationHook.scenarionShutdown(getCurrentScenarioGlobals());
		}
		return scenarioGlobalsMap.remove(Thread.currentThread());
	}

	public void cleanUp() {
		scenarioGlobalsMap.clear();
	}

	public boolean isOpenResultingReportAfterCompletion() {
		return openResultingReportAfterCompletion;
	}

	public void setOpenResultingReportAfterCompletion(boolean openResultingReportAfterCompletion) {
		this.openResultingReportAfterCompletion = openResultingReportAfterCompletion;
	}
	
	public Thread getReportingShutdownHook(final String testResultFolder) {
		if(shutDownHook == null) {			
			final boolean openBrowser = isOpenResultingReportAfterCompletion();
			shutDownHook = new Thread() {
				public void run(){
					try {
						String generatedReportLocation = CucumberReportGenerator.generateReport(testResultFolder);
						
						if(openBrowser) {
							if(generatedReportLocation == null) {
								//see if there is a index.html in the root of the test result folder
								String defaultPrettyReportLocation = testResultFolder + "/index.html";
								File file = new File(defaultPrettyReportLocation);
								if(file.exists()) {
									generatedReportLocation = defaultPrettyReportLocation;
								}
							}
							if(generatedReportLocation != null) {	
								try {									
									Desktop.getDesktop().browse(new URI("file:///" + generatedReportLocation));
								}
								catch (HeadlessException he) {
									log.warn("This process is running headless, can't open the report in a browser.");
								}
							}
						}
						
					} catch (Exception e) {
						throw new RuntimeException("Exception whole trying to generate the Cucumber report", e);
					}
				}
			};
		}
		return shutDownHook;
	}

}
