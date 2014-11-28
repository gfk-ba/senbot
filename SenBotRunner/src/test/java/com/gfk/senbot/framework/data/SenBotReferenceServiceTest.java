package com.gfk.senbot.framework.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gfk.senbot.framework.context.SenBotContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/cucumber.xml"})
public class SenBotReferenceServiceTest {
    
	public static final String BUTTON_TEST_PAGE_URL = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/exampleButton.html";

    @Test
    public void testSeleniumManager_NameSpace() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();

        String noNameSpace = "This is just a plain old string;!§$%&/()=?${}";
        String namespacenizedNoNameSpace = referenceService.namespaceString(noNameSpace);
        assertTrue(noNameSpace.equals(namespacenizedNoNameSpace));

        String nameSpace = referenceService.NAME_SPACE_PREFIX + " a String ";
        String namespacenizedNameSpace = referenceService.namespaceString(nameSpace);
        assertTrue(!nameSpace.equals(namespacenizedNameSpace));
    }

    @Test(expected = ScenarioNameSpaceAccessOutsideScenarioScopeException.class)
    public void testSeleniumManager_ScenarioNameSpace_outsideScenario() {
    	SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();
    	
    	String scenarioNameSpace = referenceService.SCENARIO_NAME_SPACE_PREFIX + " a String ";
    	referenceService.namespaceString(scenarioNameSpace);
    }

    @Test
    public void testSeleniumManager_ScenarioNameSpace() {
    	SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();
    	SenBotContext.getSenBotContext().getCucumberManager().startNewScenario();
    	
    	String noNameSpace = "This is just a plain old string;!§$%&/()=?${}";
    	String namespacenizedNoNameSpace = referenceService.namespaceString(noNameSpace);
    	assertTrue(noNameSpace.equals(namespacenizedNoNameSpace));
    	
    	String nameSpace = referenceService.NAME_SPACE_PREFIX + " a String ";
    	String scenarioNameSpace = referenceService.SCENARIO_NAME_SPACE_PREFIX + " a String ";
    	String nameSpacedValue = referenceService.namespaceString(nameSpace);
    	String scenarioNameSpacedValue = referenceService.namespaceString(scenarioNameSpace);
    	assertFalse("The returned value should have been changed", scenarioNameSpace.equals(scenarioNameSpacedValue));
    	assertFalse("The scenarion name space should be different than the normal name space", nameSpacedValue.equals(scenarioNameSpacedValue));

    	SenBotContext.getSenBotContext().getCucumberManager().stopNewScenario();
    }

    @Test
    public void testSeleniumManager_NameSpace_multipleCalls() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();

        String nameSpace = " a String " + referenceService.NAME_SPACE_PREFIX + " a String ";
        assertEquals(referenceService.namespaceString(nameSpace), referenceService.namespaceString(nameSpace));
    }

    @Test
    public void testSeleniumManager_NameSpace_midOfString() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();

        String nameSpace = " a String " + referenceService.NAME_SPACE_PREFIX + " a String ";
        assertNotEquals(referenceService.namespaceString(nameSpace), referenceService);
    }

    @Test
    public void testSeleniumManager_NameSpace_multiOccurance() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();

        String nameSpace = referenceService.NAME_SPACE_PREFIX + " a String " + referenceService.NAME_SPACE_PREFIX + " a String ";
        assertNotEquals(referenceService.namespaceString(nameSpace), referenceService);
    }

    @Test
    public void testAddReference() throws IllegalAccessException, InvocationTargetException {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();
        MockRefObject expected = new MockRefObject();
        referenceService.addReference(MockRefObject.class, "mock_ref", expected);

        MockRefObject found = (MockRefObject) referenceService.getReference(MockRefObject.class, "mock_ref");

        assertEquals(expected, found);
    }
    
    

    @Test
    public void testGetElementLocatorForElementReference_withApendix() throws Exception {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();
        String result = referenceService.getElementLocatorForElementReference("Ref by ID", "//apendix").toString();
        assertEquals("By.xpath: //*[@id='idRef']//apendix", result);

        result = referenceService.getElementLocatorForElementReference("Ref by XPath", "//apendix").toString();
        assertEquals("By.xpath: //*XPathRef//apendix", result);
    }
    
    @Test
    public void testAddReference_propertiesConfigures() throws IOException, IllegalAccessException, InvocationTargetException {
    	SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();
        MockRefObject created = new MockRefObject();
        created.setName("name1");
        created.setType("a type");
        
        referenceService.addReference(MockRefObject.class, "mock_ref", created);
        
        MockRefObject found = referenceService.getReference(MockRefObject.class, "mock_ref");
        
        assertNotSame("The name should have been overwritten buy the contributed properties file", "name1", found.getName());
        assertEquals("The name should be changed to that in the contributed properties file", "The new name", found.getName());
        assertEquals("The type should stay the same", "a type", found.getType());
    }

}