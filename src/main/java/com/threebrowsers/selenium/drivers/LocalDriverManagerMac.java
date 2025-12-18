package com.threebrowsers.selenium.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class LocalDriverManagerMac extends LocalDriverManager {

    public LocalDriverManagerMac(String browser, boolean headless) {
        super(browser, headless);
    }

    @Override
    public WebDriver createDriver() {
        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
            throw new IllegalStateException("Este driver solo es válido en macOS.");
        }

        SafariOptions safariOptions = new SafariOptions();

        System.out.println("[INFO] SafariDriver iniciado en macOS.");

        WebDriver driver = new SafariDriver(safariOptions);

        setupDriver(driver);
        return driver;
    }
}
