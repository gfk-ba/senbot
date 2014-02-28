package com.gfk.senbot.documenter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
//import org.apache.maven.plugins.annotations.Mojo;

import com.gfk.senbot.framework.context.SenBotContext;

import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

@Mojo
public class SenBotDocumenter extends AbstractMojo {
	
	private Map<String, StepDef> availableStepDefs = new HashMap<String, StepDef>();
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		SenBotDocumenter senBotDocumenter = new SenBotDocumenter();
		senBotDocumenter.generateDocumentation();
		
	}

	public void generateDocumentation() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
			
		
//		ClassLoader senbotClassPath = SenBotContext.class.getClassLoader();
//	    Set<URL> senbotUrls = ClasspathHelper.forClassLoader(senbotClassPath);

//	    ClassLoader cucumberApiClassPath = When.class.getClassLoader();
//	    Set<URL> cucmberurls = ClasspathHelper.forClassLoader(cucumberApiClassPath);
	    
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.addUrls(SenBotContext.class.getProtectionDomain().getCodeSource().getLocation());
//	    Reflections gfkReflections = new Reflections(cb);

//	    ConfigurationBuilder cbcuce = new ConfigurationBuilder();
	    cb.addUrls(When.class.getProtectionDomain().getCodeSource().getLocation());
	    Reflections cucumberStepDefReflections = new Reflections(cb);
		
		
		Set<Class<?>> allCucumberStepMethodAnnotations = cucumberStepDefReflections.getTypesAnnotatedWith(StepDefAnnotation.class);
		
		for(Class parentClass : allCucumberStepMethodAnnotations) {
			if(parentClass.toString().contains("cucumber.api.java")) {
				//it's a given/when/then/and/but annotation
				continue;
			}
			else {
				Method[] allMethods = parentClass.getMethods();
				for(Method m : allMethods) {					
					Annotation[] annotations = m.getAnnotations();
					for(Annotation an : annotations) {
						if(an.toString().contains("cucumber.api.java")) {
							
							Method valueFetcher = an.getClass().getMethod("value");
							Object invoke = valueFetcher.invoke(an);
							if(invoke instanceof String) {
								StepDef stepDef = new StepDef(parentClass, m, an);
								
								availableStepDefs.put(stepDef.getStepRegexValue(), stepDef);
							}
							else {
								// if the Annotation argument is not just a string it won't be a @When, @Then, @Given, @And, @But
								// most likeley it will be a @Before @After
							}
						}
					}
				}
			}
		}
	}
	
	public void outputToFile() throws IOException {
		File outputFolderFile = new File("target/stepdef.html");
		FileWriter fileWriter = new FileWriter(outputFolderFile);
		
		XMLOutputter outputter = new XMLOutputter();
		outputter.output(generateHtml(), fileWriter);
		
		fileWriter.close();
	}
	
	private Document generateHtml() {
		Document document = new Document();
		Element html = new Element("html");
		document.setRootElement(html);
		Element head = new Element("head");
		Element body = new Element("body");
		html.addContent(head);
		html.addContent(body);
		
		head.addContent(new Element("title").setText("Step definition documentation"));
		
		body.addContent(new Element("h1").setText("All steps on the classpath"));
		Element list = new Element("ol");
		body.addContent(list);
		
		for(String key : availableStepDefs.keySet()) {
			StepDef stepDef = availableStepDefs.get(key);
			Element listItem = new Element("li");
			list.addContent(listItem);
			
			listItem.addContent(new Element("h3").setText(key));
			listItem.addContent(new Element("br"));
			listItem.addContent("found at: " + stepDef.getFullMethodName());
		}
		
		return document;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
		
	}

}
