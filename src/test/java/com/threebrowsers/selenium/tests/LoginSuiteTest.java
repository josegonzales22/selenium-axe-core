package com.threebrowsers.selenium.tests;

import annotations.browsers.chrome.ChromeDesktop;
import annotations.browsers.edge.EdgeDesktop;
import annotations.browsers.firefox.FirefoxDesktop;
import annotations.browsers.safari.SafariLocal;
import com.threebrowsers.selenium.drivers.BaseDriver;
import com.threebrowsers.selenium.drivers.LocalDriverManager;
import com.threebrowsers.selenium.steps.LoginSteps;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginSuiteTest extends BaseTest {

    @Test
    @Order(1)
    @ChromeDesktop
    void testInChrome() {
        String browser = "chrome";
        executeTest("Chrome Desktop", new LocalDriverManager(browser, headlessLocal), (driver, test) -> {
            new LoginSteps(driver, "Chrome_Desktop", test).execute(baseUrlLocal);
        });
    }

    @Test
    @Order(2)
    @EdgeDesktop
    void testInEdge() {
        String browser = "edge";
        executeTest("Edge Desktop", new LocalDriverManager(browser, headlessLocal), (driver, test) -> {
            new LoginSteps(driver, "Edge_Desktop", test).execute(baseUrlLocal);
        });
    }

    @Test
    @Order(3)
    @FirefoxDesktop
    void testInFirefox() {
        String browser = "firefox";
        executeTest("Firefox Desktop", new LocalDriverManager(browser, headlessLocal), (driver, test) -> {
            new LoginSteps(driver, "Firefox_Desktop", test).execute(baseUrlLocal);
        });
    }

    @Test
    @Order(4)
    @EnabledOnOs(OS.MAC)
    @DisplayName("Safari")
    @SafariLocal
    void testInSafari() {
        String browser = "safari";
        BaseDriver driverManager = new com.threebrowsers.selenium.drivers.mac.LocalDriverManagerMac(browser, false);

        executeTest("Safari Desktop", driverManager, (driver, test) -> {
            new LoginSteps(driver, "Safari_Desktop", test).execute(baseUrlLocal);
        });
    }
}