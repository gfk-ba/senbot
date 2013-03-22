package com.gfk.senbot.framework.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.opera.core.systems.OperaDriver;

/**
 * 
 * This class represents the environment setting a test can run on. It reflects
 * the Selenium node setting. This class generates the WebDriver object
 * 
 */
public class TestEnvironment {

    private static Logger         log = LoggerFactory.getLogger(TestEnvironment.class);

    /**
     * Ensure every thread will have it's own web driver
     */
    private WebDriverThreadLocale threadedWebDriver;
    /**
     * Constant for Firefox
     */
    public static final String    FF  = "FF";
    /**
     * Constant for Chrome
     */
    public static final String    CH  = "CH";
    /**
     * Constant for Internet Explorer
     */
    public static final String    IE  = "IE";
    /**
     * Constant for Opera
     */
    public static final String    OP  = "OP";
    /**
     * Constant for Safari
     */
    public static final String    SF  = "SF";

    private String                browser;
    private String                browserVersion;
    private Platform              os;

    /**
     * Constructor
     * 
     * @param aBrowser
     *            The browser name
     * @param aBrowserVersion
     *            The browserVersion, i.e. the name of the configuration of the
     *            Selenium Node
     * @param aOS
     *            The operating system
     */
    public TestEnvironment(String aBrowser, String aBrowserVersion, Platform aOS) {

        log.debug("TestEnvironment initiated with: browser: " + aBrowser + ", browserVersion: " + aBrowserVersion + ", OS: " + aOS);

        browser = aBrowser;
        browserVersion = aBrowserVersion;
        os = aOS;

        threadedWebDriver = new WebDriverThreadLocale(this);
    }

    /**
     * TestFramework: getBrowser
     * 
     * @return Browser string
     */
    public String getBrowser() {
        return browser;
    }

    /**
     * TestFramework: toString
     * 
     * @return String representation
     */
    public String toString() {
        return browser + " " + browserVersion + " " + os.toString();
    }

    /**
     * TestFramework: getOSName
     * 
     * @return OS
     */
    public Platform getOS() {
        return os;
    }
    
    /**
     * TestFramework: getVersion
     * 
     * @return Version string
     */
    public String getBrowserVersion() {
        return browserVersion;
    }

    /**
     * 
     * @return {@link WebDriver} for this environment and creates one if not
     *         done so already;
     */
    public WebDriver getWebDriver() {
        return threadedWebDriver.get();
    }

    /**
     * Check if the {@link WebDriver} has been accessed since the passed in
     * since
     * 
     * @param since
     *            epoch time
     * @return true if access after since, otherwise false
     */
    public boolean isWebDriverAccessedSince(long since) {
        return threadedWebDriver.isWebDriverAccessedSince(since);
    }

    /**
     * Quits the latest driver
     */
    public void cleanupDriver() {
        getWebDriver().quit();
        threadedWebDriver.set(null);
    }

    /**
     * Quits all drivers
     */
    public void cleanupAllDrivers() {

        log.debug("Cleaning up all " + threadedWebDriver.getAllDrivers().size() + " registered webdrivers for this TestEnvironment: " + this.toPrettyString());

        for (WebDriver driver : threadedWebDriver.getAllDrivers()) {
            try {
                driver.quit();
            } catch (WebDriverException webDriverEx) {
                // Already quite or unavailable.
            }
        }
        threadedWebDriver = new WebDriverThreadLocale(this);
    }

    /**
     * TestFramework: matches Compares tqwo objects of TestEnvironment and tells
     * whether they match
     * 
     * @param aEnvironment
     * @return true if objects match
     */
    public boolean matches(TestEnvironment aEnvironment) {
        return this.equals(aEnvironment, true);
    }

    /**
     * equals method overwritten
     * 
     * @param obj
     *            The object
     * @return True if object matches
     */
    @Override
    public boolean equals(Object obj) {
        return equals(obj, false);
    }

    /**
     * 
     * @param obj
     *            The object to match
     * @param matchAnyAndWindowsOs
     *            Tells that the object might be defined for Windows or Any
     * 
     * @return True if object matches
     */
    private boolean equals(Object obj, boolean matchAnyAndWindowsOs) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestEnvironment other = (TestEnvironment) obj;
        if (browser == null) {
            if (other.browser != null)
                return false;
        } else if (!browser.equals(other.browser))
            return false;
        if (browserVersion == null) {
            if (other.browserVersion != null)
                return false;
        } else if (!browserVersion.equals(other.browserVersion))
            return false;

        if (os == null) {
            if (other.os != null) {
                return false;
            }
        } else if (os != other.os) {
            if (!matchAnyAndWindowsOs || !other.os.is(os)) {
                return false;
            }

        }
        return true;
    }

    /**
     * Calculates an object hash code
     * 
     * @return The hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((browser == null) ? 0 : browser.hashCode());
        result = prime * result + ((browserVersion == null) ? 0 : browserVersion.hashCode());
        result = prime * result + ((os == null) ? 0 : os.hashCode());
        return result;
    }

    /**
     * Makes the web driver threadable
     * 
     * @author joostschouten
     */
    private class WebDriverThreadLocale extends ThreadLocal<WebDriver> {

        private final TestEnvironment testEnvironment;
        private Map<Thread, Long>     lastWebDriverAccessMap = new HashMap<Thread, Long>();
        private Set<WebDriver>        allDrivers             = new HashSet<WebDriver>();

        /**
         * Constructor
         * 
         * @param testEnvironment
         *            The test environment
         * @param webDriverConstructor
         *            The web driver constructor
         */
        public WebDriverThreadLocale(TestEnvironment testEnvironment) {
            this.testEnvironment = testEnvironment;
        }

        /**
         * Gets the web driver
         * 
         * @return The web driver
         */
        public WebDriver get() {
            WebDriver webDriver = super.get();
            if (webDriver == null) {

                log.debug("The WebDriver previously associated with this thread: " + Thread.currentThread().toString() + " has been cleared. Reinstantiate and associate it.");

                webDriver = initialValue();
                set(webDriver);
            }
            allDrivers.add(webDriver);
            getLastWebDriverAccessMap().put(Thread.currentThread(), System.currentTimeMillis());

            return webDriver;
        }

        /**
         * 
         * @return The web driver
         */
        @Override
        protected WebDriver initialValue() {
            return constructWebDriver();
        }

        /**
         * 
         * @param since
         *            Time span in [ms]
         * @return True if the web driver has been accessed in the given time
         *         span
         */
        public boolean isWebDriverAccessedSince(long since) {
            return getLastWebDriverAccessMap().get(Thread.currentThread()) != null && since < getLastWebDriverAccessMap().get(Thread.currentThread());
        }

        public Map<Thread, Long> getLastWebDriverAccessMap() {
            return lastWebDriverAccessMap;
        }

        public Collection<WebDriver> getAllDrivers() {
            return allDrivers;
        }

    }

    /**
     * @return The test environment setting as printable stgring
     */
    public String toPrettyString() {
        StringBuilder builder = new StringBuilder();
        builder.append("browser: " + browser);
        builder.append(", browser-version: " + browserVersion);
        builder.append(", operating system: " + os.toString());
        return builder.toString();
    }

    /**
     * Delegation method to construct the WebDriver
     */
    private WebDriver constructWebDriver() {
        log.debug("constructWebDriver called on TestEnvironment: " + this.toPrettyString());

        SeleniumManager seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();

        WebDriver driver = null;
        if (seleniumManager.isRunOnGrid()) {

            log.debug("Remote WebDriver should be created to run on a selenium grid for environment: " + this.toPrettyString());

            DesiredCapabilities capability = DesiredCapabilities.firefox();
            if (TestEnvironment.FF.equals(browser)) {
                capability = DesiredCapabilities.firefox();
            } else if (TestEnvironment.CH.equals(browser)) {
                capability = DesiredCapabilities.chrome();
                capability.setCapability("chrome.verbose", false);
                capability.setCapability("chrome.switches", Lists.newArrayList("disable-logging=true"));
            } else if (TestEnvironment.OP.equals(browser)) {
                capability = DesiredCapabilities.opera();
            } else if (TestEnvironment.IE.equals(browser)) {
                capability = DesiredCapabilities.internetExplorer();
            } else if (TestEnvironment.SF.equals(browser)) {
                capability = DesiredCapabilities.safari();
            } else {
                throw new IllegalArgumentException("Browser value is not correct: " + browser);
            }
            capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
            capability.setVersion(browserVersion);
            capability.setPlatform(os);

            driver = new Augmenter().augment(new RemoteWebDriver(seleniumManager.getSeleniumHub(), capability));
        } else {

            log.debug("Local WebDriver should be created to run on this local machine for environment: " + this.toPrettyString());

            if (TestEnvironment.FF.equals(browser)) {
            	FirefoxProfile p = new FirefoxProfile();
//            	p.setPreference("webdriver.log.file", SenBotContext.getSenBotContext().getTestResultsFolder() + "/firefox_console.log");
            	driver = new FirefoxDriver(p);
            } else if (TestEnvironment.CH.equals(browser)) {
                ChromeOptions chromeOptions = new ChromeOptions();
                // chromeOptions.setCapability("chrome.verbose", false);
                // chromeOptions.addArguments("--disable-logging=true");
                driver = new ChromeDriver(chromeOptions);
            } else if (TestEnvironment.OP.equals(browser)) {
                driver = new OperaDriver();
            } else if (TestEnvironment.IE.equals(browser)) {
                driver = new InternetExplorerDriver();
            } else if (TestEnvironment.SF.equals(browser)) {
                driver = new SafariDriver();
            } else {
                throw new IllegalArgumentException("Browser value is not correct: " + browser);
            }
        }

        if (seleniumManager.getImplicitTimeout() != null) {
            int timeout = seleniumManager.getImplicitTimeout();
            if (driver instanceof InternetExplorerDriver) {
                // IE is said to be much slower the the other browsers
                timeout = timeout * 2;
            }

            driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        }

        driver.manage().window().setSize(new Dimension(seleniumManager.getDefaultWindowWidth(), seleniumManager.getDefaultWindowHeight()));

        return driver;
    }

}
