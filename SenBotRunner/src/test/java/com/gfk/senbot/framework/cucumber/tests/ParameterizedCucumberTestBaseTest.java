package com.gfk.senbot.framework.cucumber.tests;

import org.junit.runner.RunWith;

import com.gfk.senbot.framework.cucumber.CucumberTestBase;
import com.gfk.senbot.framework.cucumber.ParameterizedCucumber;

import cucumber.api.junit.Cucumber;

@RunWith(ParameterizedCucumber.class)
//@CucumberOptions
@Cucumber.Options(format = {"json", "json:target/test-results/result.json"}, 
		monochrome = true, 
		glue = {"com.gfk"},
		features = {"src/test/resources/features"},
        tags = {"~@ignore", "~@to-implement", "~@random-failure", "~@broken"}, 
		strict = true)
public class ParameterizedCucumberTestBaseTest extends CucumberTestBase {

}
