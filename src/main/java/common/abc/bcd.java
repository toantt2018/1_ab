package common.abc;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.serenitybdd.core.webdriver.enhancers.BeforeAWebdriverScenario;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.webdriver.SupportedWebDriver;

public class bcd implements BeforeAWebdriverScenario {

	@Override
	public DesiredCapabilities apply(EnvironmentVariables environmentVariables, SupportedWebDriver driver, TestOutcome testOutcome, DesiredCapabilities capabilities) {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");

		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	
		return capabilities;
	}

}
