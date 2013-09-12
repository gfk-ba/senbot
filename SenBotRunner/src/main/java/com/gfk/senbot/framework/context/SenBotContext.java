package com.gfk.senbot.framework.context;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ResourceUtils;

import com.gfk.senbot.framework.data.SenBotReferenceService;

/**
 * A central SenBot context class managing all SenBot functions and modules. It follows a singleton approach where the
 * instantiation is managed by Spring in the spring/senbot-runner-beans.xml file.
 * 
 * @author joostschouten
 *
 */
public class SenBotContext {
	
	public static final String SPRING_CONFIG_LOCATION = "springConfig";

	private static Logger log = LoggerFactory.getLogger(SenBotContext.class);
	
	/**
	 * The location to the resources needed for running the tests. Eg. html files to use for testing cetrain step def behaviour, files to upload etc.
	 */
    private String runtimeResources = null;

    private static SenBotContext         senBotContextSingleton = null;
    private File                         testResultsFolder      = null;
    private final SeleniumManager        seleniumManager;
    private final CucumberManager        cucumberManager;
    private static ApplicationContext    context;

    private final SenBotReferenceService referenceService;
	public static final String SENBOT_CONTEXT_ALTERNATE_RUNTIME_RESOURCES_PROPERTY_NAME = "senbotContext.alternateRuntimeResources";
    public static final String RESOURCE_LOCATION_PREFIX = "resource_location:";

    /**
     * Constructor
     * 
     * @param testResultsFolder  The folder where to store te test results
     * @param cucumberManager    The cucumber manager object
     * @param seleniumManager    The selenium manager object
     * @param referenceService   The reference service object
     * @param alternateRuntimeResources TODO
     * @throws IOException       
     * @throws URISyntaxException 
     */
    public SenBotContext(String testResultsFolder, CucumberManager cucumberManager, SeleniumManager seleniumManager, SenBotReferenceService referenceService, String alternateRuntimeResources)
            throws IOException, URISyntaxException {
        super();
        
        this.cucumberManager = cucumberManager;
        this.seleniumManager = seleniumManager;
        this.referenceService = referenceService;

        if (StringUtils.isBlank(testResultsFolder)) {
            throw new IllegalArgumentException("The target folder for the test results cannot be blank. Refer to senbot-runner.properties");
        } else {
            // make sure the output folder exists if it does not already
            this.testResultsFolder = getOrCreateDir(testResultsFolder, true);
        }
        
        if (StringUtils.isBlank(alternateRuntimeResources)) {
            URL classPathLocation = this.getClass().getClassLoader().getResource("");
            File classPathFile = new File(new URI(classPathLocation.toExternalForm()));
            this.runtimeResources = classPathFile.getAbsolutePath();
        } else {
            this.runtimeResources = alternateRuntimeResources;
        }
        
        log.info("Initializing the SenBotContext with arguments: testResultsFolder: " + testResultsFolder + 
        		", cucumberManager: " + cucumberManager + 
        		", seleniumManager: " + seleniumManager + 
        		", referenceService: " + referenceService+ 
        		", alternateRuntimeResources: " + alternateRuntimeResources);
    }

    /**
     * Obtain the {@link SenBotContext} singleton. Instantiate it if not yet available
     * @return SenBotContext
     */
    public static SenBotContext getSenBotContext() {
        if (senBotContextSingleton == null) {
            //synchronize instantiation
            synchronized (SenBotContext.class) {
                //another null check as two threads might have passed the first null check, wait in line on the 
                //synchronized block and both instantiate the singleton.
                if (senBotContextSingleton == null) {
                	String springConfigLocation = System.getProperty(SPRING_CONFIG_LOCATION);
                	if(springConfigLocation == null) {
                		springConfigLocation = "classpath*:cucumber.xml";
                	}
                	
                    context = new ClassPathXmlApplicationContext(new String[]{springConfigLocation});
                    senBotContextSingleton = context.getBean(SenBotContext.class);
                }
            }
        }
        return senBotContextSingleton;
    }

    /**
     * Gets the bean class
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getSenBotContext().context.getBean(clazz);
    }

    /**
     * Cleanup the singleton
     */
    public static void cleanupSenBot() {
    	
    	log.info("Cleaning up the SenBotContext and unreferencing the singleton");
    	
        if (senBotContextSingleton != null) {
            senBotContextSingleton.cleanUp();
            senBotContextSingleton = null;
        }
    }

    /**
     * delegate cleanup to all managers
     */
    private void cleanUp() {
        if (getSeleniumManager() != null) {
            getSeleniumManager().cleanUp();
        }
        if (getCucumberManager() != null) {
        	getCucumberManager().cleanUp();
        }
    }

    /**
     * @return Class managing all selenium variables, drivers and environments
     */
    public SeleniumManager getSeleniumManager() {
        return seleniumManager;
    }

    /**
     * @return The folder to generate the test results in
     */
    public File getTestResultsFolder() {
        return testResultsFolder;
    }

    /**
     * @return The selenium web driver for the browser 
     */
    public static WebDriver getSeleniumDriver() {
        TestEnvironment lastAccessed = getSenBotContext().getSeleniumManager().getAssociatedTestEnvironment();
        return lastAccessed == null ? null : lastAccessed.getWebDriver();
    }

    /**
     * Get a File on the class path or file system or create it if it does not
     * exist
     */
    public static File getOrCreateDir(String url, boolean createIfNotFound) throws IOException {
        File folder = null;
        if (url.contains(ResourceUtils.CLASSPATH_URL_PREFIX)) {
            url = url.replaceAll(ResourceUtils.CLASSPATH_URL_PREFIX, "");
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            URL resource = SenBotContext.class.getClassLoader().getResource("");
            if (resource == null) {
                throw new IOException("creating a new folder on the classpath is not allowed");
            } else {
                folder = new File(resource.getFile() + "/" + url);
            }
        } else {
            folder = new File(url);
        }
        if (createIfNotFound && !folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    /**
     * @return The cucumberManager
     */
    public CucumberManager getCucumberManager() {
        return cucumberManager;
    }

    /**
     * @return The SenBotReferenceService
     */
    public SenBotReferenceService getReferenceService() {
        return referenceService;
    }

    /**
     * @return The runtimeRessources
     */
    public String getRuntimeResources() {
        return runtimeResources;
    }
}
