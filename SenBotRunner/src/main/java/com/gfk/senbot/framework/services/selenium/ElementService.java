package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.data.SenBotReferenceService;

/**
 * A util class for locating elements on a HTML page using seleniums {@link WebDriver}
 * 
 * @author joostschouten
 *
 */
public class ElementService extends BaseServiceHub {

    private static Logger      log                           = LoggerFactory.getLogger(ElementService.class);

    public static final String XPATH_LOCATOR_PREFIX          = "xpath:";
    public static final String ID_LOCATOR_PREFIX             = "id:";
    public static final String NAME_ATTRIBUTE_LOCATOR_PREFIX = "name:";
    public static final String CSS_LOCATOR_PREFIX            = "css:";

    private WebElement         until;

    /**
     * A helper method to return an element based on the locator passed in. The locator can:
     * <ul>
     * 	<li>start with {@link #XPATH_LOCATOR_PREFIX} followed by the xpath leading to the element to locate</li>
     * 	<li>start with {@link #ID_LOCATOR_PREFIX} followed by the id used on the element to locate</li>
     * 	<li>start with {@link #NAME_ATTRIBUTE_LOCATOR_PREFIX} followed by name attribute value used on the element to locate</li>
     * 	<li>Be a logical name matching a {@link By} as specified in the {@link SenBotReferenceService#getElementLocatorForElementReference(String)}</li>
     * </ul>
     * 
     * @param locatorString
     * @return The found web element
     */
    public WebElement translateLocatorToWebElement(String locatorString) {
        return this.findExpectedElement(translateLocator(locatorString));
    }

    public By translateLocator(String locatorString) {
        By locator = null;
        String lowerCaseLocatorString = locatorString.toLowerCase();
        if (lowerCaseLocatorString.startsWith(XPATH_LOCATOR_PREFIX)) {
            locator = By.xpath(locatorString.replace(XPATH_LOCATOR_PREFIX, ""));
        } else if (lowerCaseLocatorString.startsWith(ID_LOCATOR_PREFIX)) {
            locator = By.id(locatorString.replace(ID_LOCATOR_PREFIX, ""));
        } else if (lowerCaseLocatorString.startsWith(NAME_ATTRIBUTE_LOCATOR_PREFIX)) {
            locator = By.xpath("//*[@name='" + locatorString.replace(NAME_ATTRIBUTE_LOCATOR_PREFIX, "") + "']");
        } else if (lowerCaseLocatorString.startsWith(CSS_LOCATOR_PREFIX)) {
            locator = By.cssSelector(locatorString.replace(CSS_LOCATOR_PREFIX, ""));
        } else {
            locator = getReferenceService().getElementLocatorForElementReference(locatorString);
        }
        return locator;
    }

    /**
     * Find an element that is expected to be on the page. Elements that are on the page but are hidden will not be returned
     * 
     * @param by - the {@link By} to find
     * @return the matched {@link WebElement}
     * @throws AssertionError if no {@link WebElement} is found or the search times out
     */
    public WebElement findExpectedElement(By by) {
        return findExpectedElement(by, false);
    }

    /**
     * Find an element that is expected to be on the page
     * 
     * @param by - the {@link By} to find
     * @param includeHiddenElements - should hidden elements on the page be included or not
     * @return the matched {@link WebElement}
     * @throws AssertionError if no {@link WebElement} is found or the search times out
     */
    public WebElement findExpectedElement(By by, boolean includeHiddenElements) {
        waitForLoaders();
        try {
            WebElement foundElement = new WebDriverWait(getWebDriver(), getSeleniumManager().getTimeout()).until(ExpectedConditions.presenceOfElementLocated(by));
            if (includeHiddenElements || foundElement.isDisplayed()) {
                return foundElement;
            } else {
                //the element is found but not visible. Wait to see if it becomes visible
                try {
                    new WebDriverWait(getWebDriver(), getSeleniumManager().getTimeout()).until(ExpectedConditions.visibilityOfElementLocated(by));
                    return foundElement;
                } catch (WebDriverException toe) {
                    fail("Element: " + by.toString() + " is found but not visible");
                }
            }
        } catch (WebDriverException wde) {
            log.error("Expected element not found: ", wde);
            fail("Element: " + by.toString() + " not found");
        }

        return null;
    }

    public WebElement findExpectedFirstMatchedElement(By... bys) {
        return findExpectedFirstMatchedElement(0, bys);
    }

    /**
     * From the passed array of {@link By}'s return the first {@link WebElement} found. The {@link By}'s will be processed in the 
     * order that they are passed in. If no element matches any of the {@link By}'s an {@link AssertionError} is thrown causing a 
     * test method calling this method to fail.
     * 
     * @param timeoutInSeconds timeout to wait for elements to be found
     * @param bys - array of {@link By} 
     * @return The first {@link WebElement} found
     * @throws AssertionError if no {@link WebElement} is found or the search times out
     */
    public WebElement findExpectedFirstMatchedElement(int timeoutInSeconds, By... bys) {
        waitForLoaders();
        StringBuilder potentialMatches = new StringBuilder();
        for (By by : bys) {
            try {
                if (potentialMatches.length() > 0) {
                    potentialMatches.append(", ");
                }
                potentialMatches.append(by.toString());
                WebElement found = new WebDriverWait(getWebDriver(), timeoutInSeconds).until(ExpectedConditions.presenceOfElementLocated(by));
                if (found != null) {
                    return found;
                }
            } catch (WebDriverException nsee) {
                //keep checking
            }
        }
        fail("No matches found for: " + potentialMatches.toString());
        return null;
    }

    /**
     * As most browsers do not yet support XPath 2.0 which offers a matches function, we'll use this hacky 
     * Case insensitive check
     * @return
     */
    public String constructCaseInsensitiveContains(String identifier, String value) {
        return "contains(translate(" + identifier + ",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '" + value.toLowerCase() + "')";
    }

    /**
     * Check if the passed element is visible/invisible
     * @param {@link By} to be matched
     * @param visible should the element be visible (true) or invisible (false)
     */
    public void isElementVisible(By locator, boolean visible) {
        waitForLoaders();
        if (visible) {
            try {
                WebElement found = new WebDriverWait(getWebDriver(), getSeleniumManager().getTimeout()).until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (WebDriverException e) {
                fail("The element " + locator.toString() + " is not found or invisible where it is expected as present and visible");
            }
        } else {
            try {
                boolean invisible = new WebDriverWait(getWebDriver(), getSeleniumManager().getTimeout()).until(ExpectedConditions.invisibilityOfElementLocated(locator));
            } catch (WebDriverException e) {
                fail("The element " + locator.toString() + " is visible where it is expected as invisible");
            }
        }
    }

    /**
     * Tells whether the passed element exists
     * @param  {@link By} to be matched
     * @return true if the element exists
     */
    public boolean getElementExists(By locator) {
        waitForLoaders();
        List<WebElement> foundElements = getWebDriver().findElements(locator);
        return (foundElements.size() > 0);
    }

    /**
     * An application might show an icon while loading.
     * Calling this method waits for the loading icon to disappear.
     * Use this method sparsely as it consumes lots of waiting time
     */
    public void waitForLoaders() {
        if (getScenarioGlobals() != null && !getScenarioGlobals().getLoaderIndicators().isEmpty()) {
            SynchronisationService synchronisationService = new SynchronisationService();
            int continueWaiting = 0;

            // We don't know how many loader indicators we might get and we don't know in what order.
            while (continueWaiting <= 0) {
                continueWaiting = getScenarioGlobals().getLoaderIndicators().size();
                for (By loaderIndicator : getScenarioGlobals().getLoaderIndicators()) {
                    if (synchronisationService.waitForExpectedCondition(ExpectedConditions.invisibilityOfElementLocated(loaderIndicator))) {
                        continueWaiting++;
                    } else {
                        continueWaiting--;
                    }
                }
            }
        }
    }

    /**
     * Drags an element some place else
     *  
     * @param draggable The element to drag
     * @param droppable The drop aim
     * @throws InterruptedException
     */
    public void dragElementTo(String draggable, String droppable) throws InterruptedException {
        dragElementTo(draggable, droppable, 0);
    }

    /**
     * Drags an element some place else
     *  
     * @param draggable The element to drag
     * @param droppable The drop aim
     * @param waitForMillies ???
     * @throws InterruptedException
     */
    public void dragElementTo(String draggable, String droppable, int waitForMillies) throws InterruptedException {
        WebElement draggableEl = translateLocatorToWebElement(draggable);
        WebElement dragReceiver = translateLocatorToWebElement(droppable);

        Actions clickAndDrag = new Actions(getWebDriver());
        clickAndDrag.dragAndDrop(draggableEl, dragReceiver);
        clickAndDrag.perform();

        // ToDO: clarify what to do with the parameter waitForMillies
    }

    /**
     * Clicks a button.
     * Always use this method if you plan to run the tests on IE9
     * In IE9 element.click() does not in a reliable way on buttons.
     *  
     * @param button The element to click at
     */
    public void ieSaveButtonClick(WebElement button) {
        WebDriver driver = getWebDriver();
        // This shall solve the clicking problems IE9 has
        if (driver instanceof InternetExplorerDriver) {
            ((JavascriptExecutor) driver).executeScript("var tmp = arguments[0]; tmp.click()", button);
        } else {
            button.click();
        }
    }
}
