package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.util.ResourceUtils;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.context.SenBotContext;

/**
 * A selenium util class for all navigation related actions
 * 
 * @author joostschouten
 *
 */
public class NavigationService extends BaseServiceHub {
    private final ElementService seleniumElementService;

    /**
     * Constructor 
     * 
     * @param seleniumElementService A ElementService object
     */
    public NavigationService(ElementService seleniumElementService) {
        this.seleniumElementService = seleniumElementService;
    }

    /**
     * Navigates the {@link WebDriver} to the passed page
     * 
     * @param url to navigate to. This can be either a file on the file system, a http(s) url or a {@link ResourceUtils#CLASSPATH_URL_PREFIX} prefixed 
     * string indicating the page can be found on the current classpath.
     * @throws IOException
     */
    public void navigate_to_url(String url) throws IOException {
        if (url.startsWith(SenBotContext.RESOURCE_LOCATION_PREFIX)) {
            String urlLoc = getSenBotContext().getRuntimeResources() + url.replace(SenBotContext.RESOURCE_LOCATION_PREFIX, "");
            if (urlLoc.startsWith("/")) {
                urlLoc = urlLoc.replaceFirst("/", "");
            }
            url = "file:///" + urlLoc;
        }
        getWebDriver().get(url);
    }

    /**
     * Check if the current page viewed by the {@link WebDriver} matches that of the passed url. If the expectedPage holds url parameters
     * they will be used to match on. If none are provided but the current page does have them, the parameters on the current page will be ignored in the match
     * @param exectedPage
     */
    public void isCurrentlyOnPage(String exectedPage) {
        if (exectedPage.endsWith("/")) {
            exectedPage = exectedPage.substring(0, exectedPage.length() - 1);
        }

        String currentPage = getWebDriver().getCurrentUrl();
        if (currentPage.contains("?") && !exectedPage.contains("?")) {
            currentPage = currentPage.substring(0, currentPage.indexOf("?"));
        }
        if (currentPage.endsWith("/")) {
            currentPage = currentPage.substring(0, currentPage.length() - 1);
        }

        assertEquals(exectedPage, currentPage);
    }

    /**
     * Find a Element that has a attribute with a certain value and click it
     * 
     * @param attributeName 
     * @param attributeValue
     */
    public void clickElementWithAttributeValue(String attributeName, String attributeValue) {
        By xpath = By.xpath("//*[@" + attributeName + "='" + attributeValue + "']");
        WebElement element = seleniumElementService.findExpectedElement(xpath);
        assertTrue("The element you are trying to click (" + xpath.toString() + ") is not displayed", element.isDisplayed());
        element.click();
    }

    /**
     * find and click a button or input containing the passed text. Will return the first match where an input take presedence
     * over a button
     * @param text which should be contained on the button
     * @throws AssertionError if no button with this text is found
     * 
     */
    public void clickButtonWithText(String text) {
        String caseInsensitiveButtonText = seleniumElementService.constructCaseInsensitiveContains("text()", text);
        By buttonPpath = By.xpath("//button[" + caseInsensitiveButtonText + "]");
        String caseInsensitiveValueText = seleniumElementService.constructCaseInsensitiveContains("@value", text);
        By submitPath = By.xpath("//input[@type='submit'][" + caseInsensitiveValueText + "]");
        By inputButtonPath = By.xpath("//input[@type='button'][" + caseInsensitiveValueText + "]");
        By inputResetPath = By.xpath("//input[@type='reset'][" + caseInsensitiveValueText + "]");
        WebElement foundButton = seleniumElementService.findExpectedFirstMatchedElement(buttonPpath, submitPath, inputButtonPath, inputResetPath);
        if (foundButton == null) {
            foundButton = seleniumElementService.findExpectedFirstMatchedElement(2, buttonPpath, submitPath, inputButtonPath, inputResetPath);
        }
        foundButton.click();
    }

    /**
     * Has a page been requested for this selenium session. This method is available to prevent scripts for waiting for a cetrain condition
     * if no url has been requested yet. If true you know you can just proceed and not check for any state as none exists
     * @return
     */
    public boolean isInitialPageRequested() {
        String currentUrl = getWebDriver().getCurrentUrl();
        if (StringUtils.isBlank(currentUrl) || 
        		(
        			!currentUrl.toLowerCase().startsWith("http") && 
        			!currentUrl.toLowerCase().startsWith("file")
        		)
        	) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Hovers the mouse over the given element
     * @param locator The element locator
     */
    public void mouseHoverOverElement(By locator) {
        SynchronisationService synchronisationService = new SynchronisationService();

        synchronisationService.waitAndAssertForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(locator));

        WebElement element = getWebDriver().findElement(locator);
        Actions builder = new Actions(getWebDriver());
        Actions hoverOverRegistrar = builder.moveToElement(element);
        hoverOverRegistrar.perform();
    }
    
    public void windowScrollBottom() {
    	JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }
    
    public void windowScrollTop() {
    	JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        js.executeScript("window.scrollTo(0, 0)");
    }
}
