package com.threebrowsers.selenium.drivers;

import org.openqa.selenium.WebDriver;
import java.time.Duration;

public abstract class BaseDriver {

    protected WebDriver driver;

    public abstract WebDriver createDriver();

    protected void setupDriver(WebDriver driver) {
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}