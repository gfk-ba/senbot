package com.gfk.senbotdemo;

import org.openqa.selenium.By;

import com.gfk.senbot.framework.data.GenericUser;
import com.gfk.senbot.framework.data.ReferenceServicePopulator;
import com.gfk.senbot.framework.data.SenBotReferenceService;
import com.gfk.senbotdemo.cucumber.views.TestPage1;

public class SenBotReferenceDataPopulator implements ReferenceServicePopulator {

    public void populate(SenBotReferenceService referenceService) {
    	
        /**
         * Setup all users references for testing
         */
        referenceService.addUser("admin", new GenericUser("1", "admin", "12345_password"));
        referenceService.addUser("regular user", new GenericUser(null, "regular", "6789_password"));
        
        /**
         * Define the views
         */
        referenceService.addPageRepresentationReference("Test page1", TestPage1.class);
        
        /**
         * setup all page references
         */
        referenceService.addPageReference("Some page name", "/path/to/page.html");
        referenceService.addPageReference("Some other page name", "/path/to/other/page.html");

        /**
         * Reference locators
         */
        referenceService.addLocatorReference("Logical table name", By.xpath("//div[@id='containerId']//table"));
        referenceService.addLocatorReference("Logical header name", By.id("header"));

    }

}
