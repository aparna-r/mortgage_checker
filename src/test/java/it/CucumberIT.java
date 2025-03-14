package it;

import com.mortgages.testassignment.Application;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@CucumberContextConfiguration
@Suite
@IncludeEngines("cucumber")
@SelectPackages("it")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "it")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "html:target/cucumber-reports/it.html")
@SpringBootTest(classes = Application.class, webEnvironment = DEFINED_PORT)
public class CucumberIT {
}
