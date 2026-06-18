package com.threebrowsers.selenium.drivers.mac;

import com.threebrowsers.selenium.drivers.LocalDriverManager;
import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class LocalDriverManagerMac extends LocalDriverManager {

    private final String browser;

    public LocalDriverManagerMac(String browser, boolean headless) {
        super(browser, headless);
        this.browser = browser.toLowerCase();
    }

    @Override
    public WebDriver createDriver() {
        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
            throw new IllegalStateException("[ERROR] Safari can only run on agents/runners with real macOS");
        }

        if (super.headless) {
            Logs.warning("Safari does not natively support headless mode. It will force visible execution.");
        }

        if (!browser.equals("safari")) {
            return super.createDriver();
        }

        SafariOptions safariOptions = new SafariOptions();

        Logs.info("SafariDriver started safely on macOS");
        driver = new SafariDriver(safariOptions);

        setupDriver(driver);

        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            Logs.info("Safari could not be maximized graphically: " + e.getMessage());
        }

        return driver;
    }
}