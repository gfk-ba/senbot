package com.gfk.senbot.framework.cucumber.tests;

import org.junit.runner.RunWith;

import com.gfk.senbot.framework.cucumber.CucumberTestBase;
import com.gfk.senbot.framework.cucumber.ParameterizedCucumber;

import cucumber.api.junit.Cucumber;

//@formatter:off
@RunWith(ParameterizedCucumber.class)
@Cucumber.Options(
        format = {"pretty", "html:target/test-results/focus"},
        monochrome = true,
        glue = "com.gfk",
        features = "src/test/resources/features", 
        strict = true,
        tags = {"~@ignore", "~@to-implement", "@focus"})
//@formatter:on
public class CucumberFocusTest extends CucumberTestBase {

}
