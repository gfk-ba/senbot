#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#set( $symbol_quote = '"' )
package ${package}.senbot.cucumber.tests.webapp;

import org.junit.runner.RunWith;

import com.gfk.senbot.framework.cucumber.CucumberTestBase;
import com.gfk.senbot.framework.cucumber.ParameterizedCucumber;

import cucumber.api.junit.Cucumber;

@RunWith(ParameterizedCucumber.class)
@Cucumber.Options(
        format = {"pretty", "html:target/test-results" + WebAppChapter1Test.PATH}, 
        monochrome = true, 
        features = "features" + WebAppChapter1Test.PATH, 
        glue = {"com.gfk.senbot", ${symbol_quote}${package}${symbol_quote}}, 
        tags = {"~@ignore", "~@to-implement"}, 
        strict = true)
public class WebAppChapter1Test extends CucumberTestBase {
	
	public static final String PATH = "/WebApp/Chapter1";

}
