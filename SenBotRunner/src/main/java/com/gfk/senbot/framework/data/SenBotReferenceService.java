package com.gfk.senbot.framework.data;

import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.support.PageFactory;

import com.gfk.senbot.framework.context.CucumberManager;
import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;

/**
 * A class to define global variables and reference them by a logical human readable way. 
 * This will help you instead to write tests like:
 * <pre>
 * 		Given I am looking at page https://localhost:8080/some_path/to/a/user_page.html?name=valie123456
 * </pre>
 * as like this: (which is easier to read)
 * <pre>
 * 		Given I am looking at the "user page"
 * </pre>
 * 
 * This is achieved by linking the url to the "user page" name as follows:
 * <pre>
 * static{
 * 		referenceService.addPageReference("user page", "https://localhost:8080/some_path/to/a/user_page.html?name=valie123456");
 * }
 * </pre>
 * 
 * The same reference mechanism is available for element locators to avoid long xpath in the tests and for {@link GenericUser}'s so that you can say things like
 * <pre>
 * 		Given I am loged in as a "admin" user
 * </pre>
 *  
 * 
 * @author joostschouten
 *
 */
public class SenBotReferenceService {

    public static final String              NAME_SPACE_PREFIX                   = "NS:";

    public static final String              SCENARIO_NAME_SPACE_PREFIX          = "SNS:";

    /**
     * Map to store generic reference objects by their respective class and name
     */
    private Map<Class, Map<String, Object>> referenceMaps                       = new HashMap<Class, Map<String, Object>>();
    private Map<String, GenericUser>        userReferenceToUsersMap             = new CaseinsensitiveMap<GenericUser>();
    private Map<String, String>             pageReferenceToPageUrlMap           = new CaseinsensitiveMap<String>();
    /**
     * Allows for referencing a page or view instantatable by the {@link PageFactory}
     */
    private Map<String, Class>              pageRepresentationMap               = new CaseinsensitiveMap<Class>();
    private Map<String, By>                 elementReferenceToElementLocatorMap = new CaseinsensitiveMap<By>();
    private ThreadLocal<String>             nameSpaceThreadLocale               = new ThreadLocal<String>() {
                                                                                    @Override
                                                                                    protected String initialValue() {
                                                                                        return "NS" + new Integer(UUID.randomUUID().hashCode()).toString() + "-";
                                                                                    }
                                                                                };

	private final CucumberManager cucumberManager;

    /**
     * Constructor 
     * 
     * @param  populatorClassName
     * 
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public SenBotReferenceService(String populatorClassName,
    		CucumberManager cucumberManager) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        this.cucumberManager = cucumberManager;
		if (!StringUtils.isBlank(populatorClassName)) {
            Constructor<?> constructor = Class.forName(populatorClassName).getConstructor();
            ReferenceServicePopulator populator = (ReferenceServicePopulator) constructor.newInstance();
            populator.populate(this);
        }

    }

    /**
     * Map a {@link GenericUser} to a reference name
     * 
     * @param refName
     * @param user
     */
    public void addUser(String refName, GenericUser user) {
        userReferenceToUsersMap.put(refName, user);
    }

    /**
     * Map a {@link String} url to a reference name
     * 
     * @param pageReference
     * @param pageUrl
     */
    public void addPageReference(String pageReference, String pageUrl) {
        pageReferenceToPageUrlMap.put(pageReference, pageUrl);
    }

    /**
     * Map a {@link By} element locator to a reference name
     * 
     * @param locatorReference
     * @param locator
     * 
     * @deprecated use {@link SenBotReferenceService#addPageRepresentationReference(String, Class)} in stead to define views. Referencing elements at a global level
     * is a bad idea as it becomes congested within no time and the element context is completely lost
     */
    public void addLocatorReference(String locatorReference, By locator) {
        elementReferenceToElementLocatorMap.put(locatorReference, locator);
    }
    
    public void addPageRepresentationReference(String name, Class clazz) {
    	pageRepresentationMap.put(name, clazz);
    }

    /**
     * Find a {@link GenericUser} by its reference name
     * 
     * @param userType
     * @return {@link GenericUser}
     */
    public GenericUser getUserForUserReference(String userType) {
        GenericUser driveUser = userReferenceToUsersMap.get(userType);
        if (driveUser == null) {
            fail("No user of type '" + userType + "' is found. Available user references are: " + userReferenceToUsersMap.keySet().toString());
        }
        return driveUser;
    }

    /**
     * Find a {@link String} url by its reference name
     * @param pageReference
     * @return {@link String} representing the url to the page mapped to the passed in page name
     */
    public String getUrlForPageReference(String pageReference) {
        String url = pageReferenceToPageUrlMap.get(pageReference);
        if (url == null) {
            fail("No url is found for page name: '" + pageReference + "'. Available page references are: " + pageReferenceToPageUrlMap.keySet().toString());
        }
        return url;
    }

    public Class getPageRepresentationReference(String pageRepresentationReference) {
    	Class ret = pageRepresentationMap.get(pageRepresentationReference);
    	if (ret == null) {
    		fail("No Class is found for page/view name: '" + pageRepresentationReference + "'. Available page references are: " + pageRepresentationMap.keySet().toString());
    	}
    	return ret;
    }

    public <T> T getPageRepresentationInstance(Class<T> referenceClassType) {
    	return SenBotContext.getSenBotContext().getSeleniumManager().getViewRepresentation(referenceClassType);
    }

    /**
     * Find a {@link By} locator by its reference name
     * 
     * @param elementReference
     * @return {@link By}
     */
    public By getElementLocatorForElementReference(String elementReference) {
        By elementLocator = elementReferenceToElementLocatorMap.get(elementReference);
        if (elementLocator == null) {
            fail("No elementLocator is found for element name: '" + elementReference + "'. Available element references are: " + elementReferenceToElementLocatorMap.keySet().toString());
        }
        return elementLocator;
    }

    /**
     * Find a {@link By} locator by its reference name and add something to the xpath before the element gets returned
     * The drawback of using this method is, that all locators are converted into By.xpath
     * 
     * @param elementReference The name under which the refference is found 
     * @param apendix The part of the xpath that shall be added
     * @return {@link By}
     */
    public By getElementLocatorForElementReference(String elementReference, String apendix) {
        By elementLocator = elementReferenceToElementLocatorMap.get(elementReference);
        if (elementLocator instanceof ById) {
            String xpathExpression = elementLocator.toString().replaceAll("By.id: ", "//*[@id='") + "']" + apendix;
            elementLocator = By.xpath(xpathExpression);
        } else if (elementLocator instanceof ByXPath) {
            String xpathExpression = elementLocator.toString().replaceAll("By.xpath: ", "") + apendix;
            elementLocator = By.xpath(xpathExpression);
        } else {
            fail("ElementLocator conversion error");
        }

        if (elementLocator == null) {
            fail("No elementLocator is found for element name: '" + elementReference + "'. Available element references are: " + elementReferenceToElementLocatorMap.keySet().toString());
        }
        return elementLocator;
    }

    /**
     * Extends the name space prefix with the actual name space
     * All tests that need to ensure privacy, i.e. no other test shall mess up their data, shall 
     * use name spacing. 
     * Data that can be messed up by other tests and there fore needs to be unique shall contain the name space prefix
     * that will be replaced at run time.
     * 
     * @param plainString The string that contains the name spacing string
     * @return The namespacenized string
     * @throws RuntimeExpression In case the name spacing string lives at the wrong location
     */
    public String namespaceString(String plainString) throws RuntimeException {
    	if(plainString.startsWith(NAME_SPACE_PREFIX)) {
    		return (plainString.replace(NAME_SPACE_PREFIX, nameSpaceThreadLocale.get()));    		
    	}
    	if(plainString.startsWith(SCENARIO_NAME_SPACE_PREFIX)) {
    		if(cucumberManager.getCurrentScenarioGlobals() == null) {
    			throw new ScenarioNameSpaceAccessOutsideScenarioScopeException("You cannot fetch a Scneario namespace outside the scope of a scenario");
    		}
    		return (plainString.replace(SCENARIO_NAME_SPACE_PREFIX, cucumberManager.getCurrentScenarioGlobals().getNameSpace()));    		
    	}
    	else {
    		return plainString;
    	}
    }

    public <T> void addReference(Class<T> referenceClass, String name, Object referenceObject) {
        Map<String, T> refMap = getObjectReferenceMap(referenceClass);
        refMap.put(name, (T)referenceObject);
    }

    public <T> Map<String, T> getObjectReferenceMap(Class<T> referenceClass) {
        Map<String, T> refMap = (Map<String, T>) referenceMaps.get(referenceClass);
        if (refMap == null) {
            refMap = new HashMap<String, T>();
            referenceMaps.put(referenceClass, (Map<String, Object>) refMap);
        }
        return refMap;
    }

    public <T> T getReference(Class<T> referenceClass, String referenceName) {
        return getObjectReferenceMap(referenceClass).get(referenceName);
    }

}
