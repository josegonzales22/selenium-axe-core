package com.threebrowsers.selenium.drivers.mac;

import com.threebrowsers.selenium.drivers.LocalDriverManager;
import com.threebrowsers.selenium.utils.Logs;
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
            Logs.warning("SafariDriver solo es válido en macOS.");
        }

        if (super.headless) {
            Logs.warning("Safari NO soporta headless. Ignorando parámetro.");
        }

        SafariOptions safariOptions = new SafariOptions();

        Logs.info("SafariDriver iniciado en macOS.");

        WebDriver driver = new SafariDriver(safariOptions);

        setupDriver(driver);
        return driver;
    }
}
