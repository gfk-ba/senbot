package com.gfk.senbot;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;


@Mojo( name = "run", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST )
@Execute(phase = LifecyclePhase.TEST, goal = "integration-test")
public class SenBotMojo extends AbstractMojo {

	@Parameter(required = false, property = "features")
    private String features;

	@Parameter
	private String senBotDomain;
	
	@Component
	private MavenProject mavenProject;

	@Component
	private MavenSession mavenSession;
	
	@Parameter
	private XmlPlexusConfiguration configuration;

//	@Parameter(defaultValue = "org.apache.maven.plugins:maven-surefire-plugin:2.14")
//	private Plugin testPlugin;

	@Component
	private BuildPluginManager pluginManager;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		StringBuilder builder = new StringBuilder();
		builder.append("-Dtest=" + RunAllFeatures.class.getName());
		System.setProperty("test", RunAllFeatures.class.getName());
		if (features != null) {
			getLog().info("SenBot will run the features: " + features);
//			System.setProperty("cucumber.options", features );
			builder.append(" -Dcucumber.options=\"" + features + "\"");
		}
		if(senBotDomain != null) {
			getLog().info("SenBot will use the domain: " + senBotDomain);
			System.setProperty("senBotContext.selenium.defaultDomain", senBotDomain);			
			builder.append(" -DsenBotContext.selenium.defaultDomain=" + senBotDomain);
		}
		
		getPluginContext().putAll(System.getProperties());
		
//		System.setProperty("-Darguments", "\"" + builder.toString() + "\"");
		
//		getPluginContext().
//		Xpp3Dom xpp3Dom = toXpp3Dom(configuration);
//		executeMojo(testPlugin, "test", xpp3Dom, executionEnvironment(mavenProject, mavenSession, pluginManager));
		
	}
}
