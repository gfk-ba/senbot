package com.gfk.senbot.framework.cucumber;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.ParentRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.context.TestEnvironment;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.junit.RuntimeOptionsFactory;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.snippets.SummaryPrinter;

/**
 * This is the aggregation of the {@link Parameterized} runner, that allows to run tests in parallel with the {@link Cucumber} runner. 
 * The aggregation had to be done, as Java allows only one {@link RunWith} annotiation. This class is mostly a copy from {@link Cucumber}.
 * 
 * @author joostschouten
 *
 */
public class ParameterizedCucumber extends Parameterized {
	
	private static Logger log = LoggerFactory.getLogger(ParameterizedCucumber.class);

    private final JUnitReporter jUnitReporter;
    private final List<Runner>  children = new ArrayList<Runner>();
    private final Runtime       runtime;


    /**
     * Constructor looping though the {@link Parameterized} {@link Runner}'s and adding the {@link CucumberFeature}'s found
     * as children to each one ensuring the {@link TestEnvironment} is available for each {@link Runner}.
     * 
     * @param klass
     * @throws Throwable
     */
    public ParameterizedCucumber(Class<?> klass) throws Throwable {
        super(klass);
        
        log.debug("Initializing the ParameterizedCucumber runner");

        ClassLoader classLoader = klass.getClassLoader();
        Assertions.assertNoCucumberAnnotatedMethods(klass);

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(klass);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        runtime = new Runtime(resourceLoader, classLoader, runtimeOptions);

        jUnitReporter = new JUnitReporter(runtimeOptions.reporter(classLoader), runtimeOptions.formatter(classLoader), runtimeOptions.strict);
        
        
        List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);
        List<Runner> parameterizedChildren = super.getChildren();
        final SeleniumManager seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();

        for (int i = 0; i < parameterizedChildren.size(); i++) {
        	BlockJUnit4ClassRunner paramRunner = (BlockJUnit4ClassRunner) parameterizedChildren.get(i);
        	final TestEnvironment environment = seleniumManager.getSeleniumTestEnvironments().get(i);
        
        	log.debug("Load runners for test envrironment: " + environment.toString());
        	
            for (final CucumberFeature cucumberFeature : cucumberFeatures) {

            	
            	log.debug("Load runner for test cucumberFeature: " + cucumberFeature.getFeature().getDescription() + " on evironment " + environment.toString());
                

            	FeatureRunner environmentFeatureRunner = new FeatureRunner(cucumberFeature, runtime, jUnitReporter) {
            		
					@Override
    				public void run(RunNotifier notifier) {
						log.debug("Feature run call started on: " + getDescription().toString());
						super.run(notifier);
						log.debug("Feature run call ended on: " + getDescription().toString());
    				}
					
					/**
					 * Register the {@link TestEnvironment} with the Thread executing this Scenario
					 */
					@Override
					protected void runChild(ParentRunner child, RunNotifier notifier) {
						log.debug("Scenario run call started on: " + getDescription().toString());
						try {
							seleniumManager.associateTestEnvironment(environment);
							super.runChild(child, notifier);
							seleniumManager.deAssociateTestEnvironment();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}				
						log.debug("Scenario run call ended on: " + getDescription().toString());
					}
					
					/**
					 * Add the Selenium TestEnvironment to the name of the Feature
					 */
					@Override
					public String getName() {
						return super.getName() +  " <Selenium env - " + environment.toPrettyString() + ">";
					}
					
					@Override
					protected Description describeChild(ParentRunner child) {
						return child.getDescription();
					}
            	};
            	
				children.add(environmentFeatureRunner);
            }           
        }

    }

    /**
     * @return A list of all children of the runner
     */
    @Override
    public List<Runner> getChildren() {
        return children;
    }

    /**
     * Runs a child
     * @param child    The child you want to run
     * @param notifier The notifier 
     */
    @Override
    protected void runChild(Runner child, RunNotifier notifier) {
        child.run(notifier);
    }

    /**
     * Runs all
     * @param notifier The notifier 
     */
    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        jUnitReporter.done();
        new SummaryPrinter(System.out).print(runtime);
        jUnitReporter.close();
    }

}
