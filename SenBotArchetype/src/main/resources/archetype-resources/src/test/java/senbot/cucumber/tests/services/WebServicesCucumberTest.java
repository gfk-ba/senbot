#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#set( $symbol_quote = '"' )
package ${package}.senbot.cucumber.tests.services;

import org.junit.runner.RunWith;

import com.gfk.senbot.framework.cucumber.CucumberTestBase;

import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@Cucumber.Options(
        format = {"pretty", "html:target/test-results" + WebServicesCucumberTest.PATH}, 
        monochrome = true, 
        features = "features" + WebServicesCucumberTest.PATH, 
        glue = {"com.gfk.senbot", ${symbol_quote}${package}${symbol_quote}}, 
        tags = {"~@ignore","~@to-implement"}, 
        strict = true)
public class WebServicesCucumberTest extends CucumberTestBase {
	
	public static final String PATH = "/Services";

}
