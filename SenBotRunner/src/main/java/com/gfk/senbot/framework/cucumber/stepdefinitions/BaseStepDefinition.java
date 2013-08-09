package com.gfk.senbot.framework.cucumber.stepdefinitions;

import javax.annotation.Resource;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

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
 * @deprecated extend the {@link BaseServiceHub} directly or none at all. Either way, obtain any needed services by using the {@link Resource} annotation on the required service
 *
 */
public abstract class BaseStepDefinition extends BaseServiceHub {
	
	/**
	 * A service to help target element on a page managed by seleniums {@link WebDriver}
	 * 
	 * @deprecated add the service directly in your step definition using the {@link Resource} annotation
	 */
	@Resource
	protected ElementService seleniumElementService;
	
	/**
	 * A service to help with HTML form handling on a selenium {@link WebDriver} managed page
	 * 
	 * @deprecated add the service directly in your step definition using the {@link Resource} annotation
	 */
	@Resource
	protected FormService seleniumFormService;

	/**
	 * A service to help with page navigation on a selenium {@link WebDriver} managed browser
	 * 
	 * @deprecated add the service directly in your step definition using the {@link Resource} annotation
	 */
	@Resource
	protected NavigationService seleniumNavigationService;

	/**
	 * A service to help with management of the borwser viewport on a selenium {@link WebDriver} managed page
	 * 
	 * @deprecated add the service directly in your step definition using the {@link Resource} annotation
	 */
	@Resource
	protected TableService seleniumTableService;

	/**
	 * A service to help with connecting to a remote API
	 * 
	 * @deprecated add the service directly in your step definition using the {@link Resource} annotation
	 */
	@Resource
	protected APIAccessService apiAccessService;

}
