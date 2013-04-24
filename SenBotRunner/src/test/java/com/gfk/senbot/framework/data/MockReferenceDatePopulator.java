package com.gfk.senbot.framework.data;

import org.openqa.selenium.By;

import com.gfk.senbot.framework.context.MockExampleTablePage;
import com.gfk.senbot.framework.context.SenBotContext;

public class MockReferenceDatePopulator implements ReferenceServicePopulator {

    public static final String TABLE_TEST_PAGE_URL           = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/exampleTable.html";
    public static final String DELAYED_DISPLAY_TEST_PAGE_URL = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/delayedDisplay.html";
    public static final String COMPLEX_TABLE_TEST_PAGE_URL   = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/complexExampleTable.html";
    public static final String DRAG_DROP_TEST_PAGE_URL       = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/dragDropTestPage.html";
    public static final String TABLE_NAMESPACE_TEST_PAGE_URL = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/namespaceTest.html";
    public static final String DOUBLE_CLICK_PAGE_URL         = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/doubleClick.html";
    public static final String LOADER_TEST_PAGE_URL          = SenBotContext.RESOURCE_LOCATION_PREFIX + "/test_pages/loaderIcons.html";

    public void populate(SenBotReferenceService referenceService) {
        referenceService.addPageReference("Table page", TABLE_TEST_PAGE_URL);
        referenceService.addLocatorReference("Table locator", By.xpath("//table"));
        referenceService.addLocatorReference("Ref by ID", By.id("idRef"));
        referenceService.addLocatorReference("Ref by XPath", By.xpath("//*XPathRef"));
        referenceService.addLocatorReference("Form by XPath", By.xpath("//form"));
        referenceService.addLocatorReference("Form by ID", By.id("form"));
        
        referenceService.addPageRepresentationReference("table page", MockExampleTablePage.class);
    }

}
