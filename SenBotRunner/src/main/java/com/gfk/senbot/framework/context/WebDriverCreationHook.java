package com.gfk.senbot.framework.context;

import org.openqa.selenium.WebDriver;

public interface WebDriverCreationHook {

  void webdriverInitialized(WebDriver constructWebDriver);

}
