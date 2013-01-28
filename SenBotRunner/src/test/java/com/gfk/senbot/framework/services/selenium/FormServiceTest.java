package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.context.TestEnvironment;
import com.gfk.senbot.framework.data.*;

public class FormServiceTest extends AbstractSenbotServiceTest {

    @Test
    public void testFillFormField_idLocator() throws IOException {
        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/exampleForm.html");

        String expected = "Some text";
        seleniumFormService.fillFormField_locator(ElementService.ID_LOCATOR_PREFIX + "textField", expected);

        WebElement formElement = seleniumElementService.findExpectedElement(By.id("textField"));
        assertEquals(expected, formElement.getAttribute("value"));
    }

    @Test
    public void testFillFormField_Locator_clearOnFill() throws IOException {
        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/exampleForm.html");

        String expected = "Some text";
        seleniumFormService.fillFormField_locator(ElementService.ID_LOCATOR_PREFIX + "textField", expected);
        seleniumFormService.fillFormField_locator(ElementService.ID_LOCATOR_PREFIX + "textField", expected);

        WebElement formElement = seleniumElementService.findExpectedElement(By.id("textField"));
        assertEquals(expected, formElement.getAttribute("value"));
    }

    @Test
    public void testFillFormField_Locator_passwordField() throws IOException {
        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + "//test_pages/exampleForm.html");

        String expected = "Some text";
        seleniumFormService.fillFormField_locator(ElementService.ID_LOCATOR_PREFIX + "passwordField", expected);

        WebElement formElement = seleniumElementService.findExpectedElement(By.id("passwordField"));
        assertEquals(expected, formElement.getAttribute("value"));
    }

    @Test
    public void testFillFormField_Locator_textareaField() throws IOException {
        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + "//test_pages/exampleForm.html");

        String expected = "Some text";
        seleniumFormService.fillFormField_locator(ElementService.ID_LOCATOR_PREFIX + "areaField", expected);

        WebElement formElement = seleniumElementService.findExpectedElement(By.id("areaField"));
        assertEquals(expected, formElement.getAttribute("value"));
    }

    @Test
    public void testFillFormField_Locator_textareaFieldNameSpace() throws IOException {
        seleniumNavigationService.navigate_to_url(SenBotContext.RESOURCE_LOCATION_PREFIX + "//test_pages/exampleForm.html");
   
        String expected = SenBotReferenceService.NAME_SPACE_PREFIX + "Some text";
        seleniumFormService.fillFormField_locator(ElementService.ID_LOCATOR_PREFIX + "areaField", expected);
        
        WebElement formElement = seleniumElementService.findExpectedElement(By.id("areaField"));
        assertNotEquals(expected, formElement.getAttribute("value"));
        
        assertEquals(SenBotContext.getSenBotContext().getReferenceService().namespacenizeString(expected), formElement.getAttribute("value"));
    }
}
