package config;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverInit {

    public DriverInit() {
        // hide implicit constructor
    }

    public static RemoteWebDriver initializeWebDriver() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        URL seleniumServerUrl = new URL("http://localhost:4444/wd/hub");
        return new RemoteWebDriver(seleniumServerUrl, options);
    }
}
