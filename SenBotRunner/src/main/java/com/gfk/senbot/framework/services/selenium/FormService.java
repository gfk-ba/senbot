package com.gfk.senbot.framework.services.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.context.SenBotContext;

/**
 * A util class for all form related selenium actions
 * 
 * @author joostschouten
 *
 */
public class FormService extends BaseServiceHub {
	
	private final ElementService seleniumElementService;

	public FormService(ElementService seleniumElementService){
		this.seleniumElementService = seleniumElementService;
	}

	/**
	 * Fill out a form field with the passed value
	 * 
	 * @param locator as specified in {@link ElementService#translateLocatorToWebElement(String)}
	 * @param value the value to fill the field with
	 * @return the {@link WebElement} representing the form field
	 */
	public WebElement fillFormField_locator(String locator, String value) {
		WebElement fieldEl = seleniumElementService.translateLocatorToWebElement(locator);
        fieldEl.clear();
		fieldEl.sendKeys(getReferenceService().namespaceString(value));
		return fieldEl;
	}

}
