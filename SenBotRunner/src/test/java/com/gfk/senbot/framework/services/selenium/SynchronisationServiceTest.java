package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.gfk.senbot.framework.context.SenBotContext;

public class SynchronisationServiceTest extends AbstractSenbotServiceTest {

    public static final String TEST_PAGE_URL         = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/synchronisation.html";
    public static final String EXAMPLE_FORM_PAGE_URL = "/test_pages/namespaceTest.html";

    @Test
    public void testWaitForExpectedCondition() throws Exception {
        seleniumNavigationService.navigate_to_url(TEST_PAGE_URL);

        assertTrue(seleniumSynchronisationService.waitForExpectedCondition(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div[id='text']")), seleniumManager.getTimeout()));
        seleniumElementService.ieSaveButtonClick(driver.findElement(By.cssSelector("button[id='delayedDisplayButton']")));
        assertTrue(seleniumSynchronisationService.waitForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='text']")), seleniumManager.getTimeout()));
    }

    @Test
    public void testWaitAndAssertForExpectedCondition() throws Exception {
        seleniumNavigationService.navigate_to_url(TEST_PAGE_URL);

        seleniumSynchronisationService.waitAndAssertForExpectedCondition(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div[id='text']")), seleniumManager.getTimeout());
        seleniumElementService.ieSaveButtonClick(driver.findElement(By.cssSelector("button[id='delayedDisplayButton']")));
        seleniumSynchronisationService.waitAndAssertForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='text']")), seleniumManager.getTimeout());
    }

    @Test
    public void testCheckForExpectedCondition() throws Exception {
        seleniumNavigationService.navigate_to_url(TEST_PAGE_URL);

        assertTrue(seleniumSynchronisationService.checkForExpectedCondition(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div[id='text']"))));
        seleniumElementService.ieSaveButtonClick(driver.findElement(By.cssSelector("button[id='delayedDisplayButton']")));
        assertTrue(seleniumSynchronisationService.checkForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='text']"))));
    }

    @Test
    public void testCheckAndAssertForExpectedCondition() throws Exception {
        seleniumNavigationService.navigate_to_url(TEST_PAGE_URL);

        seleniumSynchronisationService.checkAndAssertForExpectedCondition(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div[id='text']")));
        seleniumElementService.ieSaveButtonClick(driver.findElement(By.cssSelector("button[id='delayedDisplayButton']")));
        seleniumSynchronisationService.checkAndAssertForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='text']")));
    }

    @Test
    public void testWaitAndSwitchToNewBrowserWindow() throws Exception {
        seleniumNavigationService.navigate_to_url(TEST_PAGE_URL);

        seleniumSynchronisationService.checkAndAssertForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id='textField']")));
        driver.findElement(By.cssSelector("input[id='textField']")).sendKeys("file:///" + senBotContext.getRuntimeResources() + EXAMPLE_FORM_PAGE_URL);

        seleniumElementService.ieSaveButtonClick(driver.findElement(By.cssSelector("button[id='openBrowserWindow']")));
        seleniumSynchronisationService.waitAndSwitchToNewBrowserWindow(seleniumManager.getTimeout());
        assertEquals(1, driver.findElements(By.xpath("//*[contains(text(),'Namespace test page')]")).size());

        driver.close();
        seleniumSynchronisationService.switchToMainWindow();
    }

    @Test
    public void testSwitchToMainWindow() throws Exception {
        seleniumNavigationService.navigate_to_url(TEST_PAGE_URL);

        seleniumSynchronisationService.checkAndAssertForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id='textField']")));
        driver.findElement(By.cssSelector("input[id='textField']")).sendKeys("file:///" + senBotContext.getRuntimeResources() + EXAMPLE_FORM_PAGE_URL);

        seleniumElementService.ieSaveButtonClick(driver.findElement(By.cssSelector("button[id='openBrowserWindow']")));
        seleniumSynchronisationService.waitAndSwitchToNewBrowserWindow(seleniumManager.getTimeout());
        assertEquals(1, driver.findElements(By.xpath("//*[contains(text(),'Namespace test page')]")).size());

        seleniumSynchronisationService.switchToMainWindow();
        assertEquals(1, driver.findElements(By.cssSelector("input[id='textField']")).size());

        seleniumSynchronisationService.switchToLatestPopupWindow();

        assertEquals(1, driver.findElements(By.xpath("//*[contains(text(),'Namespace test page')]")).size());
        driver.close();

        seleniumSynchronisationService.switchToMainWindow();
    }
}
