package com.threebrowsers.selenium.tests;

import annotations.browsers.chrome.ChromeDesktop;
import annotations.browsers.edge.EdgeDesktop;
import annotations.browsers.firefox.FirefoxDesktop;
import annotations.browsers.safari.SafariLocal;
import annotations.browsers.safari.SafariCloud;
import com.threebrowsers.selenium.drivers.BaseDriver;
import com.threebrowsers.selenium.drivers.LocalDriverManager;
import com.threebrowsers.selenium.drivers.RemoteDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CrossBrowserSuiteTest extends BaseTest {

    @Test
    @Order(1)
    @DisplayName("Chrome")
    @ChromeDesktop
    void testInChrome() throws InterruptedException {
        String currentBrowser = "chrome";
        BaseDriver driverManager = new LocalDriverManager(currentBrowser, headlessLocal);
        executeTest(currentBrowser, driverManager);
    }

    @Test
    @Order(2)
    @DisplayName("Edge")
    @EdgeDesktop
    void testInEdge() throws InterruptedException {
        String currentBrowser = "edge";
        BaseDriver driverManager = new LocalDriverManager(currentBrowser, headlessLocal);
        executeTest(currentBrowser, driverManager);
    }

    @Test
    @Order(3)
    @DisplayName("Firefox")
    @FirefoxDesktop
    void testInFirefox() throws InterruptedException {
        String currentBrowser = "firefox";
        BaseDriver driverManager = new LocalDriverManager(currentBrowser, headlessLocal);
        executeTest(currentBrowser, driverManager);
    }

    @Test
    @Order(4)
    @EnabledOnOs(OS.MAC)
    @DisplayName("Safari")
    @SafariLocal
    void testInSafari() throws InterruptedException {
        String currentBrowser = "safari";

        if (!isMacOS()) return;
        BaseDriver driverManager =
                new com.threebrowsers.selenium.drivers.mac.LocalDriverManagerMac(
                        "safari", false);

        executeTest(currentBrowser, driverManager);
    }


    @Test
    @Order(5)
    @DisplayName("Safari cloud")
    //@Disabled("No Credentials")
    @SafariCloud
    void testInSafariCloud() throws InterruptedException {
        String currentBrowser = "safari cloud";
        BaseDriver driverManager = new RemoteDriverManager();
        executeTest(currentBrowser, driverManager);
    }

}
