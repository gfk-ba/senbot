package com.gfk.senbot.framework.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gfk.senbot.framework.context.SenBotContext;

public class SenBotReferenceServiceTest {
    public static final String BUTTON_TEST_PAGE_URL = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/exampleButton.html";

    @Test
    public void testSeleniumManager_NameSpace() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();

        String noNameSpace = "This is just a plain old string;!§$%&/()=?${}";
        String namespacenizedNoNameSpace = referenceService.namespacenizeString(noNameSpace);
        assertTrue(noNameSpace.equals(namespacenizedNoNameSpace));

        String nameSpace = referenceService.NAME_SPACE_PREFIX + " a String ";
        String namespacenizedNameSpace = referenceService.namespacenizeString(nameSpace);
        assertTrue(!nameSpace.equals(namespacenizedNameSpace));
    }

    @Test
    public void testSeleniumManager_NameSpace_multipleCalls() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();

        String nameSpace = " a String " + referenceService.NAME_SPACE_PREFIX + " a String ";
        assertEquals(referenceService.namespacenizeString(nameSpace), referenceService.namespacenizeString(nameSpace));
    }

    @Test
    public void testSeleniumManager_NameSpace_midOfString() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();

        String nameSpace = " a String " + referenceService.NAME_SPACE_PREFIX + " a String ";
        assertNotEquals(referenceService.namespacenizeString(nameSpace), referenceService);
    }

    @Test
    public void testSeleniumManager_NameSpace_multiOccurance() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();

        String nameSpace = referenceService.NAME_SPACE_PREFIX + " a String " + referenceService.NAME_SPACE_PREFIX + " a String ";
        assertNotEquals(referenceService.namespacenizeString(nameSpace), referenceService);
    }

    @Test
    public void testAddReference() {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();
        MockRefObject expected = new MockRefObject();
        referenceService.addReference(MockRefObject.class, "mock_ref", expected);

        MockRefObject found = (MockRefObject) referenceService.getReference(MockRefObject.class, "mock_ref");

        assertEquals(expected, found);
    }

    @Test
    public void testGetElementLocatorForElementReference() throws Exception {
        SenBotReferenceService referenceService = SenBotContext.getSenBotContext().getReferenceService();
        String result = referenceService.getElementLocatorForElementReference("Ref by ID", "//apendix").toString();
        assertEquals("By.xpath: //*[@id='idRef']//apendix", result);

        result = referenceService.getElementLocatorForElementReference("Ref by XPath", "//apendix").toString();
        assertEquals("By.xpath: //*XPathRef//apendix", result);
    }

}