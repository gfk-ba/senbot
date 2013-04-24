package com.gfk.senbot.framework.context;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MockExampleTablePage {
	
	@FindBy(id = "row1")
	public WebElement table_Row_1;

	@FindBy(id = "something_rather")
	public WebElement non_existing;

	@FindBy(xpath = "//td[text()='new value']")
	public WebElement New_value_cell;

}
