package com.gfk.senbot.framework.cucumber;

import gherkin.formatter.Formatter;
import gherkin.formatter.JSONFormatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Parameterized;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.context.SeleniumManager;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.context.TestEnvironment;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.formatter.FormatterFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.junit.RuntimeOptionsFactory;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.snippets.SummaryPrinter;

/**
 * This is the aggregation of the {@link Parameterized} runner, that allows to
 * run tests in parallel with the {@link Cucumber} runner. The aggregation had
 * to be done, as Java allows only one {@link RunWith} annotiation. This class
 * is mostly a copy from {@link Cucumber}.
 * 
 * @author joostschouten
 * 
 */
public class ParameterizedCucumber extends Parameterized {

	private static Logger log = LoggerFactory.getLogger(ParameterizedCucumber.class);

	private final JUnitReporter jUnitReporter;
	private final List<Runner> children = new ArrayList<Runner>();
	private final Runtime runtime;

	/**
	 * Constructor looping though the {@link Parameterized} {@link Runner}'s and
	 * adding the {@link CucumberFeature}'s found as children to each one
	 * ensuring the {@link TestEnvironment} is available for each {@link Runner}
	 * .
	 * 
	 * @param klass
	 * @throws Throwable
	 */
	public ParameterizedCucumber(Class<?> klass) throws Throwable {
    	
    	super(klass);
        
        log.debug("Initializing the ParameterizedCucumber runner");

        final ClassLoader classLoader = klass.getClassLoader();
        Assertions.assertNoCucumberAnnotatedMethods(klass);

//        Class<? extends Annotation>[] annotationClasses = new Class[]{CucumberOptions.class, Options.class};
//        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(klass, annotationClasses);
        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(klass);
        final RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
//        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
//        runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
        runtime = new Runtime(resourceLoader, classLoader, runtimeOptions);
        
        final ThreadAwareFormatter threadAwareWrappedFormatter = new ThreadAwareFormatter(runtimeOptions, classLoader);
        
        
        Reporter threadAwareReporter = new ThreadAwareReporter(threadAwareWrappedFormatter, classLoader, runtimeOptions);
        
		
		runtimeOptions.formatters.clear();
		runtimeOptions.formatters.add(threadAwareWrappedFormatter);
		
        jUnitReporter = new JUnitReporter(threadAwareReporter, threadAwareWrappedFormatter, runtimeOptions.strict);
        //overwrite the reporter so we can alter the path to write to
        
        List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);
        List<Runner> parameterizedChildren = super.getChildren();
        final SeleniumManager seleniumManager = SenBotContext.getSenBotContext().getSeleniumManager();

        for (int i = 0; i < parameterizedChildren.size(); i++) {
        	BlockJUnit4ClassRunner paramRunner = (BlockJUnit4ClassRunner) parameterizedChildren.get(i);
        	final TestEnvironment environment = seleniumManager.getSeleniumTestEnvironments().get(i);
        
        	log.debug("Load runners for test envrironment: " + environment.toString());
        	
            for (final CucumberFeature cucumberFeature : cucumberFeatures) {
            	Feature originalFeature = cucumberFeature.getGherkinFeature();
            	
            	log.debug("Load runner for test cucumberFeature: " + originalFeature.getDescription() + " on evironment " + environment.toString());
                
            	ThreadPoolFeatureRunnerScheduler environmentFeatureRunner = new ThreadPoolFeatureRunnerScheduler(environment, 
            			cucumberFeature, 
            			runtime, 
            			jUnitReporter);
            	
            	setScheduler(environmentFeatureRunner);
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
	 * 
	 * @param child
	 *            The child you want to run
	 * @param notifier
	 *            The notifier
	 */
	@Override
	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}

	/**
	 * Runs all
	 * 
	 * @param notifier
	 *            The notifier
	 */
	@Override
	public void run(RunNotifier notifier) {
		log.debug("Run called on ParameterizedCucumber");
		super.run(notifier);
		jUnitReporter.done();
		new SummaryPrinter(System.out).print(runtime);
		jUnitReporter.close();
		log.debug("Run finished on ParameterizedCucumber");
	}

	/**
	 * A thread aware {@link Reporter} wrapper to be able to deal with the parallel multithreaded environment
	 * 
	 * @author joostschouten
	 *
	 */
	private final class ThreadAwareReporter implements Reporter {
		private final ThreadAwareFormatter threadAwareWrappedFormatter;
		private final ClassLoader classLoader;
		private final RuntimeOptions runtimeOptions;
		ThreadLocal<Reporter> threadLocal;

		private ThreadAwareReporter(ThreadAwareFormatter threadAwareWrappedFormatter, 
				final ClassLoader classLoader,
				final RuntimeOptions runtimeOptions) {
			this.threadAwareWrappedFormatter = threadAwareWrappedFormatter;
			this.classLoader = classLoader;
			this.runtimeOptions = runtimeOptions;
			threadLocal = new ThreadLocal<Reporter>() {
        		@Override
        		protected Reporter initialValue() {
        			return runtimeOptions.reporter(classLoader);
        		}
        	};
		}

		private Reporter getWrapped() {
			return threadLocal.get();
		}

		@Override
		public void write(String arg0) {
			getWrapped().write(arg0);
		}

		@Override
		public void result(Result arg0) {
			Formatter wrapped = threadAwareWrappedFormatter.getWrapped();
			if(wrapped instanceof JSONFormatter) {
				((JSONFormatter) wrapped).result(arg0);
			}
			else {
				getWrapped().result(arg0);
			}
		}

		@Override
		public void match(Match arg0) {
			getWrapped().match(arg0);
		}

		@Override
		public void embedding(String arg0, byte[] arg1) {
			getWrapped().embedding(arg0, arg1);
		}

		@Override
		public void before(Match arg0, Result arg1) {
			getWrapped().before(arg0, arg1);
		}

		@Override
		public void after(Match arg0, Result arg1) {
			getWrapped().after(arg0, arg1);
			
		}
	}

	private final class ThreadAwareFormatter implements Formatter, Reporter {
		private final FormatterFactory FORMATTER_FACTORY = new FormatterFactory();
		private final RuntimeOptions runtimeOptions;
		private final ClassLoader classLoader;
		private ThreadLocal<Formatter> wrapped;
		private Collection<Formatter> known = new HashSet<Formatter>();

		private ThreadAwareFormatter(final RuntimeOptions runtimeOptions, final ClassLoader classLoader) {
			this.runtimeOptions = runtimeOptions;
			this.classLoader = classLoader;
			wrapped = new ThreadLocal<Formatter>() {
        		@Override
        		protected Formatter initialValue() {
        			Formatter created = FORMATTER_FACTORY.create("json:target/test-results/result_" + Thread.currentThread().getId() + ".json");
        			known.add(created);
					return created;
//        			Formatter formatter = runtimeOptions.formatter(classLoader);
//        			return formatter;
        		}
        	};
		}

		public Formatter getWrapped() {
			return wrapped.get();
		}

		@Override
		public void background(Background arg0) {
			getWrapped().background(arg0);
			
		}

		@Override
		public void close() {
			for(Formatter formatter : known) {				
				formatter.close();
			}
			
		}

		@Override
		public void done() {
			for(Formatter formatter : known) {				
				formatter.done();
			}
		}

		@Override
		public void eof() {
			getWrapped().eof();
		}

		@Override
		public void examples(Examples arg0) {
			getWrapped().examples(arg0);
			
		}

		@Override
		public void feature(Feature arg0) {
			getWrapped().feature(arg0);
			
		}

		@Override
		public void scenario(Scenario arg0) {
			getWrapped().scenario(arg0);
			
		}

		@Override
		public void scenarioOutline(ScenarioOutline arg0) {
			getWrapped().scenarioOutline(arg0);
			
		}

		@Override
		public void step(Step arg0) {
			getWrapped().step(arg0);
			
		}

		@Override
		public void syntaxError(String arg0, String arg1, List<String> arg2, String arg3, Integer arg4) {
			getWrapped().syntaxError(arg0, arg1, arg2, arg3, arg4);
			
		}

		@Override
		public void uri(String arg0) {
			getWrapped().uri(arg0);
		}

		@Override
		public void after(Match arg0, Result arg1) {
			Reporter wrappedReporter = getWrappedReporter();
			if(wrappedReporter != null) {
				wrappedReporter.after(arg0, arg1);
			}
		}
		
		private Reporter getWrappedReporter() {
			if(Reporter.class.isAssignableFrom(getWrapped().getClass())) {
				return (Reporter) getWrapped();
			}
			return null;
		}

		@Override
		public void before(Match arg0, Result arg1) {
			Reporter wrappedReporter = getWrappedReporter();
			if(wrappedReporter != null) {
				wrappedReporter.before(arg0, arg1);
			}
			
		}

		@Override
		public void embedding(String arg0, byte[] arg1) {
			Reporter wrappedReporter = getWrappedReporter();
			if(wrappedReporter != null) {
				wrappedReporter.embedding(arg0, arg1);
			}
			
		}

		@Override
		public void match(Match arg0) {
			Reporter wrappedReporter = getWrappedReporter();
			if(wrappedReporter != null) {
				wrappedReporter.match(arg0);
			}
			
		}

		@Override
		public void result(Result arg0) {
			Reporter wrappedReporter = getWrappedReporter();
			if(wrappedReporter != null) {
				wrappedReporter.result(arg0);
			}
			
		}

		@Override
		public void write(String arg0) {
			Reporter wrappedReporter = getWrappedReporter();
			if(wrappedReporter != null) {
				wrappedReporter.write(arg0);
			}
			
		}
	}

	/**
	 * Allow for Parallelisation so that each fature can be run in a different
	 * thread
	 * 
	 * @author joostschouten
	 * 
	 */
	private static class ThreadPoolFeatureRunnerScheduler extends FeatureRunner implements RunnerScheduler {
		private ExecutorService executor;
		private TestEnvironment environment;
		private CucumberFeature cucumberFeature;
		private JUnitReporter jUnitReporter;

		public ThreadPoolFeatureRunnerScheduler(TestEnvironment environment, 
				CucumberFeature cucumberFeature,
				Runtime runtime, 
				JUnitReporter jUnitReporter) throws InitializationError {
			super(cucumberFeature, runtime, jUnitReporter);
			this.environment = environment;
			this.cucumberFeature = cucumberFeature;
			this.jUnitReporter = jUnitReporter;
			
			Integer defaultNumThreads = SenBotContext.getSenBotContext().getCucumberManager().getParallelFeatureThreads();
			String threads = System.getProperty("junit.parallel.threads", defaultNumThreads.toString());
			int numThreads = Integer.parseInt(threads);
			executor = Executors.newFixedThreadPool(numThreads);
		}

		@Override
		public void run(RunNotifier notifier) {
			log.debug("Scenario run call started on: " + getDescription().toString());
			try {
				SenBotContext.getSenBotContext().getSeleniumManager().associateTestEnvironment(environment);
				
				super.run(notifier);
				SenBotContext.getSenBotContext().getSeleniumManager().deAssociateTestEnvironment();
				environment.cleanupDriver();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			log.debug("Scenario run call finished on: " + getDescription().toString());
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

		@Override
		public void finished() {
			executor.shutdown();
			try {
				executor.awaitTermination(10, TimeUnit.MINUTES);
			} catch (Exception exc) {
			}
		}

		@Override
		public void schedule(Runnable childStatement) {
			executor.submit(childStatement);
		}
	}

}
