package com.gfk.senbot;

import org.junit.runner.RunWith;

import com.gfk.senbot.framework.cucumber.CucumberTestBase;
import com.gfk.senbot.framework.cucumber.ParameterizedCucumber;

import cucumber.api.junit.Cucumber;


@RunWith(ParameterizedCucumber.class)
@Cucumber.Options(
        format = {"pretty", "html:target/test-results"},
        monochrome = false,
        features = {"./"},
        glue =  "com.gfk",
        strict = true,
        tags = {"~@ignore", "~@to-implement", "~@random-failure", "~@login", "~@broken"})
public class RunAllFeatures  extends CucumberTestBase{

}
