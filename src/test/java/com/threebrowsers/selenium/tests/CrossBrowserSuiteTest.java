package com.threebrowsers.selenium.tests;

import annotations.Smoke;
import com.threebrowsers.selenium.drivers.BaseDriver;
import com.threebrowsers.selenium.drivers.LocalDriverManager;
import com.threebrowsers.selenium.drivers.LocalDriverManagerMac;
import com.threebrowsers.selenium.drivers.RemoteDriverManager;
import com.threebrowsers.selenium.steps.StepsFlow;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CrossBrowserSuiteTest extends BaseTest {
    private String currentBrowser;

    @Test
    @Order(1)
    @DisplayName("Chrome local")
    @Smoke
    void testInChrome() throws InterruptedException {
        runLocalTest("chrome");
    }

    @Test
    @Order(2)
    @DisplayName("Edge local")
    @Smoke
    void testInEdge() throws InterruptedException {
        runLocalTest("edge");
    }

    @Test
    @Order(3)
    @DisplayName("Firefox local")
    @Smoke
    void testInFirefox() throws InterruptedException {
        runLocalTest("firefox");
    }

    @Test
    @Order(4)
    @EnabledOnOs(OS.MAC)
    @DisplayName("Safari local")
    @Smoke
    void testInSafari() throws InterruptedException {
        runLocalTest("safari");
    }


    @Test
    @Order(5)
    @DisplayName("Safari cloud")
    @Disabled()
    @Smoke
    void testInSafariCloud() throws InterruptedException {
        runRemoteTest();
    }

    private void runLocalTest(String browser) throws InterruptedException {
        currentBrowser = browser;
        boolean headless = Boolean.parseBoolean(localConfig.getOrDefault("headless", "false"));

        boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

        // Manejo de Safari en Windows
        if (browser.equalsIgnoreCase("safari") && !isMac) {
            System.out.println("[INFO] Safari solo se ejecuta en macOS. Test omitido.");
            Assumptions.assumeTrue(isMac, "Safari solo se ejecuta en macOS");
            return;
        }

        BaseDriver driverManager = browser.equalsIgnoreCase("safari")
                ? new LocalDriverManagerMac(browser, headless)
                : new LocalDriverManager(browser, headless);

        try {
            driver = driverManager.createDriver();
            String baseUrl = localConfig.get("base.url");
            StepsFlow steps = new StepsFlow(driver, browser, test);

            steps.executeFlow(baseUrl);
        } catch (Exception e) {
            test.fail("[ERROR] Error en la prueba de " + browser.toUpperCase() + ": " + e.getMessage());
            throw e;
        }
    }

    private void runRemoteTest() throws InterruptedException {
        currentBrowser = "safari_cloud";
        BaseDriver driverManager = new RemoteDriverManager();

        try {
            driver = driverManager.createDriver();
            String baseUrl = remoteConfig.get("base.url");
            StepsFlow steps = new StepsFlow(driver, "safari_cloud", test);

            steps.executeFlow(baseUrl);
        } catch (Exception e) {
            test.fail("[ERROR] Error en Safari remoto : " + e.getMessage());
            throw e;
        }
    }
}
