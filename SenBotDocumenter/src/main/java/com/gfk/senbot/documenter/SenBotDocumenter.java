package com.gfk.senbot.documenter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.gfk.senbot.framework.context.SenBotContext;

import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

public class SenBotDocumenter {
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		generateDocumentation();
		
	}

	public static void generateDocumentation() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		ClassLoader senbotClassPath = SenBotContext.class.getClassLoader();
	    Set<URL> senbotUrls = ClasspathHelper.forClassLoader(senbotClassPath);
//	    for(URL url : senbotUrls) {
//	      System.out.println("a url: " + url);
//	    }

	    ClassLoader cucumberApiClassPath = When.class.getClassLoader();
	    Set<URL> cucmberurls = ClasspathHelper.forClassLoader(cucumberApiClassPath);
//	    for(URL url : cucmberurls) {
//	    	System.out.println("a cucmberurls url: " + url);
//	    }
	    
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.addUrls(SenBotContext.class.getProtectionDomain().getCodeSource().getLocation());
	    Reflections gfkReflections = new Reflections(cb);

	    ConfigurationBuilder cbcuce = new ConfigurationBuilder();
	    cb.addUrls(When.class.getProtectionDomain().getCodeSource().getLocation());
	    Reflections cucumberStepDefReflections = new Reflections(cb);
		
		
//	    new Reflections(prefix, ClasspathHelper.);
//		Reflections gfkReflections = new Reflections(new ConfigurationBuilder()
//		s	.addUrls(senbotUrls)
//	        .addUrls(ClasspathHelper.forPackage("com.gfk"))
//	        .setScanners(new ResourcesScanner()));
//		Reflections cucumberStepDefReflections = new Reflections(new ConfigurationBuilder()
//			.addUrls(cucmberurls)
//			.addUrls(ClasspathHelper.forPackage("cucmber.api.java"))
//			.setScanners(new ResourcesScanner()));
		
		Set<Class<?>> allCucumberStepMethodAnnotations = cucumberStepDefReflections.getTypesAnnotatedWith(StepDefAnnotation.class);
		
		for(Class annotationType : allCucumberStepMethodAnnotations) {
			if(annotationType.toString().contains("cucumber.api.java")) {
				//it's a given/when/then/and/but annotation
				continue;
			}
			else {
				Method[] allMethods = annotationType.getMethods();
				for(Method m : allMethods) {					
					Annotation[] annotations = m.getAnnotations();
					for(Annotation an : annotations) {
						if(an.toString().contains("cucumber.api.java")) {
							
							Method valueFetcher = an.getClass().getMethod("value");
							Object invoke = valueFetcher.invoke(an);
							if(invoke instanceof String) {
								
								String stepRegexMatcher = (String) invoke;
								String funtion = an.annotationType().getSimpleName();
								
								Type[] genericParameterTypes = m.getGenericParameterTypes();
								for(Type type : genericParameterTypes) {
									System.out.println(type);
								}
								
								System.out.println("");
								System.out.println(funtion + " " + stepRegexMatcher);
								System.out.println("");
								
								System.out.println("Found method: " + annotationType.toString() + "." + m.getName() + " with annotations: " + an.toString());
							}
							else {
								//if the Annotation argument is not just a string it won't be a @When, @Then, @Given, @And, @But
								//most likeley it will be a @Before @After
							}
						}
					}
				}
			}
			
			
//			System.out.println("Checking for methods annotated with: " + annotationType.getCanonicalName());
//			Set<Method> annotatedMethods = gfkReflections.getMethodsAnnotatedWith(annotationType);
//			for(Method method : annotatedMethods) {
//				Annotation annotation = method.getAnnotation(annotationType);
//				System.out.println("found annotationh: " + annotation);				
//			}
//			
		}
	}

}
