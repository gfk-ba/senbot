package com.gfk.senbot.framework.cucumber;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.context.TestEnvironment;

/**
 * Basis class for all cucumber test runners
 * 
 * @author joostschouten
 *
 */
public class CucumberTestBase {
	
	private static Logger log = LoggerFactory.getLogger(CucumberTestBase.class);

    /**
     * Ok, this is an ugly hack so our {@link ParameterizedCucumber} runner extending {@link Parameterized} 
     * defined in the {@link RunWith} annotation will not complain  about "No runnable methods"
     */
    @Test
    @Ignore
    public void test() {
        assertTrue(true);
    }

    /**
     * Detect if the test is {@link RunWith} annotated with typedef {@link ParameterizedCucumber}, if not, instantiate the {@link TestEnvironment}. 
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void setUpTest() throws Exception {
    	
    	log.debug("@BeforeClass initiated");
    	
        SeleniumManager seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();
        TestEnvironment associatedEnvironment = seleniumManager.getAssociatedTestEnvironment();
        if (associatedEnvironment == null) {
            TestEnvironment environment = seleniumManager.getSeleniumTestEnvironments().get(0);
            seleniumManager.associateTestEnvironment(environment);
        }
    }

    /**
     * Cleanup the SenBot instance
     * @throws Exception
     */
    @AfterClass
    public static void tearDown() throws Exception {
    	
    	SeleniumManager seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();
    	if(seleniumManager.getAssociatedTestEnvironment() != null) {
    		seleniumManager.deAssociateTestEnvironment();
    	}
    	
        SenBotContext.cleanupSenBot();
    }

    /**
     * Picks the browsers on which to run the tests
     */
    @Parameters
    public static List<Object[]> getParameters() throws IOException {
        SeleniumManager seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();
        final List<TestEnvironment> seleniumTestEnvironments = seleniumManager.getSeleniumTestEnvironments();

        List<Object[]> params = new ArrayList<Object[]>() {

            /**
             * By calling the underlying seleniumTestEnvironment.get(index) we make sure our
             * globally available test environment matches that of the one returned wrapped in the Object[]
             */
            @Override
            public Object[] get(int index) {
                seleniumTestEnvironments.get(index);
                return super.get(index);
            }
        };

        for (TestEnvironment env : seleniumTestEnvironments) {
            params.add(new Object[]{env});
        }

        return params;
    }

}
