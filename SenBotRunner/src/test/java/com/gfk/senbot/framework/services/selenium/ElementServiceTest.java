package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.data.MockReferenceDatePopulator;

public class ElementServiceTest extends AbstractSenbotServiceTest {
    public static final String BUTTON_TEST_PAGE_URL = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/exampleButton.html";

    @Test(expected = AssertionError.class)
    public void testFindExpectedElement_hiddenElement() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.DELAYED_DISPLAY_TEST_PAGE_URL);

        WebElement foundText = seleniumElementService.findExpectedElement(By.id("text"));
    }

    @Test
    public void testFindExpectedElement_delayedDisplay() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.DELAYED_DISPLAY_TEST_PAGE_URL);
        seleniumElementService.ieSaveButtonClick(seleniumElementService.translateLocatorToWebElement(ElementService.ID_LOCATOR_PREFIX + "delayedDisplayButton"));

        WebElement foundText = seleniumElementService.translateLocatorToWebElement(ElementService.ID_LOCATOR_PREFIX + "text");
        assertNotNull(foundText);
        assertEquals("I should show after a delay", foundText.getText());
    }

    @Test
    public void testFindExpectedElement_hiddenExpectedElement() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.DELAYED_DISPLAY_TEST_PAGE_URL);

        WebElement foundText = seleniumElementService.findExpectedElement(By.id("text"), true);
        assertNotNull(foundText);
        assertFalse(foundText.isDisplayed());
    }

    @Test
    public void testInterpretLocator_referenceName() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement foundLocator = seleniumElementService.translateLocatorToWebElement("Table locator");
        assertNotNull(foundLocator);
    }

    @Test
    public void testInterpretLocator_nameAttribute() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement foundLocator = seleniumElementService.translateLocatorToWebElement(ElementService.NAME_ATTRIBUTE_LOCATOR_PREFIX + "cell_2_1");
        assertNotNull(foundLocator);
        assertEquals("Table cell 3", foundLocator.getText());
    }

    @Test(expected = AssertionError.class)
    public void testInterpretLocator_nonExistingName() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        seleniumElementService.translateLocatorToWebElement(ElementService.NAME_ATTRIBUTE_LOCATOR_PREFIX + "I_dont_exist");
    }

    @Test
    public void testInterpitLocator_xpath() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement foundLocator = seleniumElementService.translateLocatorToWebElement(ElementService.XPATH_LOCATOR_PREFIX + "//table");
        assertNotNull(foundLocator);
    }

    @Test
    public void testInterpitLocator_css() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement foundElement = seleniumElementService.translateLocatorToWebElement(ElementService.CSS_LOCATOR_PREFIX + "#row1.odd");
        assertNotNull(foundElement);
        assertEquals("tr", foundElement.getTagName());
    }

    @Test(expected = AssertionError.class)
    public void testInterpitLocator_nonExistingXpath() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        seleniumElementService.translateLocatorToWebElement(ElementService.XPATH_LOCATOR_PREFIX + "//table//table");
    }

    @Test
    public void testInterpitLocator_id() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        WebElement foundLocator = seleniumElementService.translateLocatorToWebElement(ElementService.ID_LOCATOR_PREFIX + "exampleTable");
        assertNotNull(foundLocator);
    }

    @Test(expected = AssertionError.class)
    public void testInterpitLocator_wrongId() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        seleniumElementService.translateLocatorToWebElement(ElementService.ID_LOCATOR_PREFIX + "non_existing_id");
    }

    @Test
    public void testIsElementVisible_visibleAndFound() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        seleniumElementService.isElementVisible(By.xpath("//table"), true);
    }

    @Test(expected = AssertionError.class)
    public void testIsElementVisible_visibleAndNotFound() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        seleniumElementService.isElementVisible(By.xpath("//table//table"), true);
    }

    @Test(expected = AssertionError.class)
    public void testIsElementVisible_invisibleAndFound() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        seleniumElementService.isElementVisible(By.xpath("//table"), false);
    }

    @Test
    public void testIsElementVisible_invisibleAndNotFound() throws IOException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.TABLE_TEST_PAGE_URL);
        seleniumElementService.isElementVisible(By.xpath("//table//table"), false);
    }

    @Test
    public void testDragElementToBy() throws IOException, InterruptedException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.DRAG_DROP_TEST_PAGE_URL);
        seleniumElementService.dragElementTo(By.id("draggable"), By.id("drop"));
        seleniumElementService.isElementVisible(By.id("droppedSuccess"), true);
    }

    @Test
    public void testDragElementTo() throws IOException, InterruptedException {
        seleniumNavigationService.navigate_to_url(MockReferenceDatePopulator.DRAG_DROP_TEST_PAGE_URL);
        seleniumElementService.dragElementTo(ElementService.ID_LOCATOR_PREFIX + "draggable", ElementService.ID_LOCATOR_PREFIX + "drop");
        seleniumElementService.isElementVisible(By.id("droppedSuccess"), true);
    }

    @Test
    public void testGetElementExists() throws Exception {
        seleniumNavigationService.navigate_to_url(BUTTON_TEST_PAGE_URL);

        assertTrue(seleniumElementService.getElementExists(By.id("text")));
        assertFalse(seleniumElementService.getElementExists(By.id("NotÉxisting")));
    }
}
