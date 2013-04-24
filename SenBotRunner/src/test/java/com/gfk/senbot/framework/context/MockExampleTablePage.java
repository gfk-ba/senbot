package com.gfk.senbot.framework.context;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MockExampleTablePage {
	
	@FindBy(id = "row1")
	public WebElement tableRow1;

}
