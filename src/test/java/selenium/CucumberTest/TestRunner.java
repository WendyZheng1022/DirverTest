package selenium.CucumberTest;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "Feature"
		, glue = {"selenium.CucumberProject"}
		, plugin = {"pretty", "html:target/cucumber-report", "json:target/cucumber-report/cucumber.json"} 
)
public class TestRunner {

}
