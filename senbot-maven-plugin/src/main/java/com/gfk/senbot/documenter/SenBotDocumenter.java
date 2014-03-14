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
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import com.gfk.senbot.framework.context.SenBotContext;

import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;

@Mojo(name = "document", 
	defaultPhase = LifecyclePhase.PACKAGE)
public class SenBotDocumenter extends AbstractMojo {
	
	@Component
    private MavenProject project;
	
	
	private Map<String, StepDef> availableStepDefs = new HashMap<String, StepDef>();
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		SenBotDocumenter senBotDocumenter = new SenBotDocumenter();
		senBotDocumenter.generateDocumentation();
		
	}

	public void generateDocumentation() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
			
		
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.addUrls(SenBotContext.class.getProtectionDomain().getCodeSource().getLocation());

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
	
	public File outputToFile() throws IOException {
		
		String buildDir = project.getBuild().getDirectory();
		File targetFolderFile = new File(buildDir);
		//ensure all folders are available for writing to
		targetFolderFile.mkdirs();
		
		File outputFolderFile = new File(buildDir + "/stepdef.html");
		FileWriter fileWriter = new FileWriter(outputFolderFile);
		
		XMLOutputter outputter = new XMLOutputter();
		outputter.output(generateHtml(), fileWriter);
		
		fileWriter.close();
		
		return outputFolderFile;
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

	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info("SenBot documenter maven plugin will scan your calsspath for all available stepdefinitions" );
			generateDocumentation();
			File outputToFile = outputToFile();
			getLog().info( availableStepDefs.size() + " SenBot available cucumber steps are found and documented in file: " + outputToFile.toURI().toString() );
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException e) {
			throw new RuntimeException(e);
		}
	}

}
