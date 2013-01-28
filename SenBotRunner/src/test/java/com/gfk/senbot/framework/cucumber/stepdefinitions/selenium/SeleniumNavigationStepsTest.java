package com.gfk.senbot.framework.cucumber.stepdefinitions.selenium;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;

import com.gfkmock.senbot.framework.cucumber.stepdefinitions.MockSeleniumNavigationStepDefinitions;

public class SeleniumNavigationStepsTest {

    @Test
    public void testPageRefresh() {
        final WebDriver mockDriver = mock(WebDriver.class);
        Navigation navigation = mock(Navigation.class);
        when(mockDriver.navigate()).thenReturn(navigation);

        MockSeleniumNavigationStepDefinitions navigationSteps = new MockSeleniumNavigationStepDefinitions(mockDriver);

        navigationSteps.pageRefresh();

        verify(navigation, times(1)).refresh();
    }

}
