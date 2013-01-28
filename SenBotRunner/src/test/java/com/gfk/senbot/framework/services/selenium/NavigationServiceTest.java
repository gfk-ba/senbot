package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.context.SenBotContext;

public class NavigationServiceTest extends AbstractSenbotServiceTest {

    public static final String TABLE_TEST_PAGE_URL  = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/exampleTable.html";
    public static final String BUTTON_TEST_PAGE_URL = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/exampleButton.html";

    /**
     * Tested method: isCurrentlyOnPage Test idea: Open a page, then call the
     * method. No assertion expected
     * 
     * @throws IOException
     */
    @Test
    public void testIsCurrentlyOnPage_same() throws IOException {
        seleniumNavigationService.navigate_to_url(TABLE_TEST_PAGE_URL);
        String testPage = SenBotContext.getSeleniumDriver().getCurrentUrl();

        seleniumNavigationService.isCurrentlyOnPage(testPage);
    }

    /**
     * Tested method: isCurrentlyOnPage Test idea: Open a page, then call the
     * method. No assertion expected Parameters: Variations in the URL path
     * regarding slashes
     * 
     * @throws IOException
     */
    @Test
    public void testIsCurrentlyOnPage_sameWithSlashDifference() throws IOException {
        String testPagesFolder = "/test_pages";

        String expected = SenBotContext.getSenBotContext().getRuntimeResources() + testPagesFolder;
        if (expected.contains("\\")) {
            expected = expected.replaceAll("\\\\", "/");
        }
        if (expected.startsWith("/")) {
            expected = expected.replaceFirst("/", "");
        }
        expected = "file:///" + expected;

        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + testPagesFolder + "/");
        seleniumNavigationService.isCurrentlyOnPage(expected);

        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + testPagesFolder);
        seleniumNavigationService.isCurrentlyOnPage(expected + "/");
    }

    /**
     * Tested method: isCurrentlyOnPage Test idea: Open a page, then call the
     * method. No assertion expected Parameters: The opened URL shall have some
     * parameters, the SenBot internal page has not
     * 
     * @throws IOException
     */
    @Test
    public void testIsCurrentlyOnPage_currentHasParams() throws IOException {
        seleniumNavigationService.navigate_to_url(TABLE_TEST_PAGE_URL + "?some=param");
        String testPage = SenBotContext.getSeleniumDriver().getCurrentUrl().replace("?some=param", "");

        seleniumNavigationService.isCurrentlyOnPage(testPage);
    }

    /**
     * 
     * ToDo: Joost?
     * 
     * @throws IOException
     */
    @Test
    public void testIsCurrentlyOnPage_equalParams() throws IOException {
        seleniumNavigationService.navigate_to_url(TABLE_TEST_PAGE_URL + "?some=param");
        String testPage = SenBotContext.getSeleniumDriver().getCurrentUrl().replace("?some=param", "");

        seleniumNavigationService.isCurrentlyOnPage(testPage);
    }

    /**
     * Tested method: isCurrentlyOnPage Test idea: Open a page, then call the
     * method. Assertion expected Parameters: The opened URL shall have no
     * parameters, the SenBot internal page has
     * 
     * @throws IOException
     */
    @Test(expected = AssertionError.class)
    public void testIsCurrentlyOnPage_expectedParams() throws IOException {

        seleniumNavigationService.navigate_to_url(TABLE_TEST_PAGE_URL);
        String testPage = SenBotContext.getSeleniumDriver().getCurrentUrl();

        seleniumNavigationService.isCurrentlyOnPage(testPage + "?some=param");
    }

    /**
     * Tested method: isCurrentlyOnPage Test idea: Open a page, then call the
     * method with another URL. Assertion expected
     * 
     * @throws IOException
     */
    @Test(expected = AssertionError.class)
    public void testIsCurrentlyOnPage_noMatch() throws IOException {
        seleniumNavigationService.navigate_to_url(TABLE_TEST_PAGE_URL);
        seleniumNavigationService.isCurrentlyOnPage("http://www.google.com");
    }

    @Test
    public void testClickElementWithAttributeValue() throws IOException {
        seleniumNavigationService.navigate_to_url(BUTTON_TEST_PAGE_URL);
        seleniumNavigationService.clickElementWithAttributeValue("title", "clickMe");

        WebElement foundTextArea = seleniumElementService.findExpectedElement(By.id("buttonClickText"));
        assertEquals("Submit button", foundTextArea.getText());
    }

    @Test
    public void testClickButtonWithText_button() throws IOException {
        assertButtonFound("Button");
    }

    @Test
    public void testClickButtonWithText_inputButtons() throws IOException {
        assertButtonFound("Input button");
    }

    @Test
    public void testClickButtonWithText_submitButtons() throws IOException {
        assertButtonFound("Submit button");
    }

    @Test
    public void testClickButtonWithText_resetButton() throws IOException {
        assertButtonFound("Reset button");
    }

    public void assertButtonFound(String buttonText) throws IOException {
        By createdTestLocator = By.id("buttonClickText");
        seleniumNavigationService.navigate_to_url(BUTTON_TEST_PAGE_URL);
        seleniumNavigationService.clickButtonWithText(buttonText);

        WebElement foundTextArea = seleniumElementService.findExpectedElement(createdTestLocator);
        assertEquals(buttonText, foundTextArea.getText());
    }

    @Test
    public void testNavigate_to_url() throws Exception {
        String pageUnderTest = "/test_pages/exampleButton.html";
        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + pageUnderTest);
        SenBotContext senBotContext = SenBotContext.getSenBotContext();

        String expectedUrl = senBotContext.getRuntimeResources().toString() + pageUnderTest;
        //To test this on windows we need to replace all backslash characers with forward slashes
        expectedUrl = expectedUrl.replaceAll("\\\\", "/");
        if (expectedUrl.startsWith("/")) {
            expectedUrl = expectedUrl.replaceFirst("/", "");
        }
        //remove any trailing slash from the url
        String foundUrl = senBotContext.getSeleniumDriver().getCurrentUrl();
        if (foundUrl.endsWith("/")) {
            foundUrl = foundUrl.substring(0, foundUrl.length() - 1);
        }
        assertEquals("file:///" + expectedUrl, foundUrl);
    }

    @Test
    public void testIsInitialPageRequested() throws IOException {
        assertFalse(seleniumNavigationService.isInitialPageRequested());
        String pageUnderTest = "/test_pages/exampleButton.html";
        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + pageUnderTest);
        assertTrue(seleniumNavigationService.isInitialPageRequested());
    }

}
