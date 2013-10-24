package com.gfk.senbotdemo.cucumber.tests;

import org.junit.runner.RunWith;

import com.gfk.senbot.framework.cucumber.CucumberTestBase;
import com.gfk.senbot.framework.cucumber.ParameterizedCucumber;
import cucumber.api.junit.Cucumber;

@RunWith(ParameterizedCucumber.class)
@Cucumber.Options(
        format = {"pretty", "html:target/test-results"}, 
        monochrome = true, 
        features = "features", 
        glue = {"com.gfk.senbot", "com.gfk.senbotdemo"}, 
        tags = {"~@ignore", "~@to-implement"}, 
        strict = true)
public class WebAppTest extends CucumberTestBase {
	
}
