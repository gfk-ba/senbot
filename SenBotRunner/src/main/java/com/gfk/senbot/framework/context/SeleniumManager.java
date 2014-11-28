package com.gfk.senbot.framework.context;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;

/**
 * Manager of all selenium related configuration and runtime variables.
 * The selenium driver is initiated here.
 * Also the settings for running on the grid or on the local machine.
 * 
 * @author joostschouten
 *
 */
public class SeleniumManager {

    private static Logger                log                      = LoggerFactory.getLogger(SeleniumManager.class);

    private String                       defaultDomain            = null;
    private URL                          seleniumHub              = null;
    private ArrayList<TestEnvironment>   seleniumTestEnvironments = new ArrayList<TestEnvironment>();

    private final Integer                defaultWindowWidth;
    private final Integer                defaultWindowHeight;
    private int                          timeout;
    private Integer                      implicitTimeout;


    private Map<Thread, TestEnvironment> associatedEnvironment    = new HashMap<Thread, TestEnvironment>();
    private WebDriverCreationHook webDriverCreationHook;

  public SeleniumManager(
    		String defaultDomain, 
    		String seleniumHubIP, 
    		String target, 
    		int defaultWindowWidth, 
    		int defaultWindowHeight, 
    		int aTimeout) throws IOException, AWTException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this(defaultDomain, seleniumHubIP, target, defaultWindowWidth, defaultWindowWidth, aTimeout, null, null);

        //this is way too annoying and has too little effect to keep moving the cursor
//        try {
        	//move the mouse to 0,50 so that it won't interfere with IE native events. 50 to avoid trigger of hotcorners
//        	Robot robot = new Robot();
//        	robot.mouseMove(0, 50);        	
//        }
//        catch (AWTException awte) {
        	//thrown when the process is running headless, just continue without moving the cursor
//        }
    }

    /**
     * {@link SeleniumManager} constructor managed by spring
     * 
     * @param defaultDomain domain the whole selenium related SenBot will work from
     * @param seleniumHubIP if running on a grid this is the hub ip to be used
     * @param target The target environements to run the selenium tests on
     * @param defaultWindowWidth browser window width to start the browser with
     * @param defaultWindowHeight browser window height to start the browser with
     * @param aTimeout implicit timeout to be used by selenium when performing a dom lookup or page refresh
     * @throws IOException
     */
    public SeleniumManager(
    		String defaultDomain, 
    		String seleniumHubIP, 
    		String target, 
    		int defaultWindowWidth, 
    		int defaultWindowHeight, 
    		int aTimeout, 
    		String implicitTimeout,
        String webdriverCreationHookClassName)
        throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

      if(!StringUtils.isBlank(webdriverCreationHookClassName)) {
        Constructor<?> constructor = Class.forName(webdriverCreationHookClassName).getConstructor();
        webDriverCreationHook = (WebDriverCreationHook) constructor.newInstance();
      }

        
        if(defaultDomain != null) {
        	if(defaultDomain.toLowerCase().startsWith("http")) {
        		this.defaultDomain = defaultDomain;        		
        	}
        	else {        		
        		this.defaultDomain = "http://" + defaultDomain;
        	}
        }
        
        this.defaultWindowWidth = defaultWindowWidth;
        this.defaultWindowHeight = defaultWindowHeight;
        this.timeout = aTimeout;
        if (!StringUtils.isBlank(implicitTimeout)) {
            this.implicitTimeout = Integer.parseInt(implicitTimeout);
        }
        this.seleniumHub = (StringUtils.isBlank(seleniumHubIP)) ? null : new URL(seleniumHubIP);

        if (StringUtils.isBlank(target)) {
            throw new IllegalArgumentException("The selenium target environment property cannot be blank. Refer to senbot-runner.properties");
        } else {
            for (String ii : target.split(";")) {
                String[] parts = ii.split(",");
				String browserVersion = parts.length > 1 ? parts[1].trim() : "ANY";
				Platform platform = Platform.valueOf(parts.length > 2 ? parts[2].trim() : "ANY");
				String locale = parts.length > 3 ? parts[3].trim() : null;
				TestEnvironment testEnvironment = new TestEnvironment(parts[0].trim(), 
                		browserVersion, 
                		platform,
                		locale);
                seleniumTestEnvironments.add(testEnvironment);
            }
        }
    }




  /**
     * Check {@link PageFactory} for how these Objects are instantiated using the {@link FindBy} and {@link FindBys} annotations.
     * Senbot will cache your objects in the Scenario globals so that they only get initialized once
     * 
     * @param T
     * @return instanciated T
     */
    public <T> T getViewRepresentation(Class<T> T) {
    	return getViewRepresentation(T, false);
    }

    /**
     * Check {@link PageFactory} for how these Objects are instantiated using the {@link FindBy} and {@link FindBys} annotations.
     * Senbot will cache your objects in the Scenario globals so that they only get initialized once
     * 
     * @param T
     * @param forceRefresh if true we will always reinstantiate
     * @return instanciated T
     */
    public <T> T getViewRepresentation(Class<T> T, boolean forceRefresh) {
    	ScenarioGlobals currentScenarioGlobals = SenBotContext.getSenBotContext().getCucumberManager().getCurrentScenarioGlobals();
    	T foundView = currentScenarioGlobals == null ? null : (T) currentScenarioGlobals.getAttribute(T.getName());
    	if(foundView == null || forceRefresh) {
    		foundView = PageFactory.initElements(getAssociatedTestEnvironment().getWebDriver(), T);
    		if(currentScenarioGlobals != null) {    			
    			currentScenarioGlobals.setAttribute(T.getName(), foundView);
    		}
    	}
    	return foundView;
    }

    /**
     * Cleaning up
     */
    protected synchronized void cleanUp() {
        for (TestEnvironment env : seleniumTestEnvironments) {
            env.cleanupAllDrivers();
        }
        seleniumTestEnvironments.clear();
    }

    /**
     * @return the deault domain
     */
    public String getDefaultDomain() {
        return defaultDomain;
    }

    /**
     * @return The URL of the Selenium Hub
     */
    public URL getSeleniumHub() {
        return seleniumHub;
    }

    /**
     * @return List of all test environments managed by the SeleniumManager 
     */
    public List<TestEnvironment> getSeleniumTestEnvironments() {
        return seleniumTestEnvironments;
    }

    /**
     * 
     * Associates Thread with TestEnvironment
     * 
     * @param environment {@link TestEnvironment} to associate with the current {@link Thread}
     * @throws Exception 
     */
    public void associateTestEnvironment(TestEnvironment aTestEnvironment) throws Exception {

        log.debug("Associate TestEnvironment: " + aTestEnvironment.toPrettyString() + " with thread: " + Thread.currentThread().toString());

        associatedEnvironment.put(Thread.currentThread(), aTestEnvironment);
    }

    /**
     * Deletes association of Thread with TestEnvironment
     * 
     * @return {@link TestEnvironment}
     * 
     * @throws InterruptedException
     */
    public TestEnvironment deAssociateTestEnvironment() throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        TestEnvironment removed = associatedEnvironment.remove(currentThread);

        if(removed != null) {        	
        	log.debug("Deassociated TestEnvironment: " + removed.toPrettyString() + " from thread: " + currentThread.toString());
        }
        else {
        	log.debug("Deassociation of TestEnvironment called without a TestEnvironment being present on thread: " + currentThread.toString());        	
        }
        
        return removed;

    }

    /**
     * @return {@link TestEnvironment} associated with the current {@link Thread}
     */
    public TestEnvironment getAssociatedTestEnvironment() {
        return associatedEnvironment.get(Thread.currentThread());
    }

    /**
     * @return Timeout in [s]
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @return defaultWindowWidth in [pixel]
     */
    public int getDefaultWindowWidth() {
        return defaultWindowWidth;
    }

    /**
     * @return defaultWindowHeight in [pixel]
     */
    public int getDefaultWindowHeight() {
        return defaultWindowHeight;
    }

    public Integer getImplicitTimeout() {
        return implicitTimeout;
    }


  public WebDriverCreationHook getWebDriverCreationHook() {
    return webDriverCreationHook;
  }
}
