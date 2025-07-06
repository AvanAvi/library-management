package com.attsw.library.management.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:features",
    glue = "com.attsw.library.management.bdd",
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class CucumberTestRunner {
}