package com.gfk.senbot.framework.cucumber.stepdefinitions;

import org.openqa.selenium.WebDriver;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.services.APIAccessService;
import com.gfk.senbot.framework.services.selenium.ElementService;
import com.gfk.senbot.framework.services.selenium.FormService;
import com.gfk.senbot.framework.services.selenium.NavigationService;
import com.gfk.senbot.framework.services.selenium.TableService;

/**
 * A base class to be extended by stepdefinitions so they have all services available from the {@link BaseServiceHub} as well
 * as the generic services provided here
 * 
 * @author joostschouten
 *
 */
public abstract class BaseStepDefinition extends BaseServiceHub {
	
	/**
	 * A service to help target element on a page managed by seleniums {@link WebDriver}
	 * 
	 * TODO: Spring autowire this
	 */
	protected ElementService seleniumElementService = SenBotContext.getBean(ElementService.class);
	
	/**
	 * A service to help with HTML form handling on a selenium {@link WebDriver} managed page 
	 * 
	 * TODO: Spring autowire this
	 */
	protected FormService seleniumFormService = SenBotContext.getBean(FormService.class);

	/**
	 * A service to help with page navigation on a selenium {@link WebDriver} managed browser 
	 * 
	 * TODO: Spring autowire this
	 */
	protected NavigationService seleniumNavigationService = SenBotContext.getBean(NavigationService.class);

	/**
	 * A service to help with management of the borwser viewport on a selenium {@link WebDriver} managed page
	 * 
	 * TODO:Spring autowire this
	 */
	protected TableService seleniumTableService = SenBotContext.getBean(TableService.class);

	/**
	 * A service to help with connecting to a remote API
	 * 
	 * TODO:Spring autowire this
	 */
	protected APIAccessService apiAccessService = SenBotContext.getBean(APIAccessService.class);

}
