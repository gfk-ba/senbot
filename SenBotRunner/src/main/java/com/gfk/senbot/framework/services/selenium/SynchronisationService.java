package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;

/**
 * Ways on how to synchronize with the web page
 * 
 * @author gdschu
 *
 */
public class SynchronisationService extends BaseServiceHub {
	
    private ThreadLocal<String> mainWindowHandle  = new ThreadLocal<String>();
    private ThreadLocal<String> popupWindowHandle = new ThreadLocal<String>();
    SeleniumManager             seleniumManager   = getSeleniumManager();

    /**
     * Waits until the expectations are full filled or timeout runs out 
     * 
     * @param condition The conditions the element should meet
     * @param timeout The timeout to wait 
     * @return True if element meets the condition
     */
    public boolean waitForExpectedCondition(ExpectedCondition<?> condition, int timeout) {
        WebDriver driver = getWebDriver();
        boolean elementFound = true;
        try {
            new WebDriverWait(driver, timeout).until(condition);
        } catch (TimeoutException e) {
            elementFound = false;
        }
        return elementFound;
    }

    /**
     * Waits until the expectations are full filled or timeout runs out using standard timeout 
     * 
     * @param condition The conditions the element should meet
     * @return True if element meets the condition
     */
    public boolean waitForExpectedCondition(ExpectedCondition<?> condition) {
        return waitForExpectedCondition(condition, seleniumManager.getTimeout());
    }

    /**
     * Waits until the expectations are met and throws an assert if not
     * 
     * @param condition The conditions the element should meet
     * @param timeout The timeout to wait 
     * @return True if element meets the condition
     */
    public void waitAndAssertForExpectedCondition(ExpectedCondition<?> condition, int timeout) {
        if (!waitForExpectedCondition(condition, timeout)) {
            fail(String.format("Element does not meet condition %1$s", condition.toString()));
        }
    }

    /**
     * Waits until the expectations are met and throws an assert if not with standard timeout
     * 
     * @param condition The conditions the element should meet
     * @return True if element meets the condition
     */
    public void waitAndAssertForExpectedCondition(ExpectedCondition<?> condition) {
        waitAndAssertForExpectedCondition(condition, seleniumManager.getTimeout());
    }

    /**
     * Checks whether the element meets the expectations
     * 
     * @param condition The conditions the element should meet
     * @return True if element meets the condition
     */
    public boolean checkForExpectedCondition(ExpectedCondition<?> condition) {
        return waitForExpectedCondition(condition, 0);
    }

    /**
     * Checks if the expectations are met and throws an assert if not
     * 
     * @param condition The conditions the element should meet
     * @return True if element meets the condition
     */
    public void checkAndAssertForExpectedCondition(ExpectedCondition<?> condition) {
        if (!waitForExpectedCondition(condition, 0)) {
            fail(String.format("Element does not meet condition %1$s", condition.toString()));
        }
    }

    /**
     * Waits for a new browser window to pop up and switches to it
     * 
     * @param timeout Timeout to wait for
     */
    public void waitAndSwitchToNewBrowserWindow(int timeout) {
        final Set<String> handles = SenBotContext.getSeleniumDriver().getWindowHandles();
        mainWindowHandle.set(SenBotContext.getSeleniumDriver().getWindowHandles().iterator().next());

        if (getWebDriver() instanceof InternetExplorerDriver) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String newWindow = (new WebDriverWait(getWebDriver(), timeout)).until(new ExpectedCondition<String>() {
            public String apply(WebDriver input) {
                if (input.getWindowHandles().size() > 1) {
                    Iterator<String> iterator = input.getWindowHandles().iterator();
                    String found = iterator.next();
                    found = iterator.next();
                    return found;
                }
                return null;
            }
        });

        assertTrue(!newWindow.equals(mainWindowHandle.get()));
        assertTrue(!newWindow.isEmpty());

        popupWindowHandle.set(newWindow);
        SenBotContext.getSeleniumDriver().switchTo().window(newWindow);
    }

    /**
     * Waits for a new browser window to pop up and switches to it with standard timeout
     * 
     */
    public void waitAndSwitchToNewBrowserWindow() {
        waitAndSwitchToNewBrowserWindow(seleniumManager.getTimeout());
    }

    /**
     * Switches back to the browsers main window 
     */
    public void switchToMainWindow() {
        if (mainWindowHandle.get() != null)
            getWebDriver().switchTo().window(mainWindowHandle.get());
    }

    /**
     * Switches back to the latest popup  window 
     */
    public void switchToLatestPopupWindow() {
        if (popupWindowHandle.get() != null)
            getWebDriver().switchTo().window(popupWindowHandle.get());
    }

}
