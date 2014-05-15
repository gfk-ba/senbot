package com.gfk.senbot.framework.services.selenium;

import static org.junit.Assert.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.gfk.senbot.framework.BaseServiceHub;

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

	public WebElement fillFormField_fromView(String viewName, String fieldName, String value) throws IllegalArgumentException, IllegalAccessException {
		WebElement fieldEl = seleniumElementService.getElementFromReferencedView(viewName, fieldName);
		fieldEl.clear();
		fieldEl.sendKeys(getReferenceService().namespaceString(value));
		return fieldEl;
	}

	public void isFormFieldOnViewSetTo(String viewName, String fieldName, String value) throws IllegalArgumentException, IllegalAccessException {
		WebElement fieldEl = seleniumElementService.getElementFromReferencedView(viewName, fieldName);
		assertEquals(getReferenceService().namespaceString(value), fieldEl.getAttribute("value"));
	}
	
	public void setSelectOptionOnView(String viewName, String elementName, String optionText) throws IllegalArgumentException, IllegalAccessException {
		WebElement elementFromReferencedView = seleniumElementService.getElementFromReferencedView(viewName, elementName);
		Select select = new Select(elementFromReferencedView);
		select.selectByVisibleText(optionText);
	}

	public void isOptionOfSelectForViewSelected(String viewName, String selectElementName, String optionText) throws IllegalArgumentException, IllegalAccessException {
		WebElement elementFromReferencedView = seleniumElementService.getElementFromReferencedView(viewName, selectElementName);
		Select select = new Select(elementFromReferencedView);
		assertEquals("Select " + selectElementName + " should have the correct option selected", optionText, select.getFirstSelectedOption().getText());
	}

	public void checkCheckboxOnView(String view, String checkboxRef) throws IllegalArgumentException, IllegalAccessException {
		WebElement elementFromReferencedView = seleniumElementService.getElementFromReferencedView(view, checkboxRef);
		if(!elementFromReferencedView.isSelected()) {
			elementFromReferencedView.click();
		}
	}

	public void uncheckCheckboxOnView(String view, String checkboxRef) throws IllegalArgumentException, IllegalAccessException {
		WebElement elementFromReferencedView = seleniumElementService.getElementFromReferencedView(view, checkboxRef);
		if(elementFromReferencedView.isSelected()) {
			elementFromReferencedView.click();
		}
		
	}

}
