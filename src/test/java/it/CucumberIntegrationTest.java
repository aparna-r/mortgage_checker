package it;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/it", plugin = "html:target/cucumber-reports/it.html")
public class CucumberIntegrationTest {
}
