package com.gfk.senbot.framework.context;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class MockWebDriverCreationHook implements WebDriverCreationHook {

  public static List<WebDriver> createdWebDrivers = new ArrayList<WebDriver>();

  @Override
  public void webdriverInitialized(WebDriver constructWebDriver) {
    createdWebDrivers.add(constructWebDriver);
  }
}
