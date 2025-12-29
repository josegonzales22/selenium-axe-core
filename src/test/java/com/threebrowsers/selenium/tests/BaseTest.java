package com.threebrowsers.selenium.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.threebrowsers.selenium.drivers.BaseDriver;
import com.threebrowsers.selenium.reports.ExtentReportManager;
import com.threebrowsers.selenium.steps.StepsFlow;
import com.threebrowsers.selenium.utils.ConfigReader;
import com.threebrowsers.selenium.utils.FileUtil;
import com.threebrowsers.selenium.utils.Logs;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import java.io.File;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    protected ExtentReports extent;
    protected ConfigReader localConfig;
    protected ConfigReader remoteConfig;

    protected ExtentTest test;
    protected WebDriver driver;

    protected Boolean headlessLocal;
    protected Boolean headlessRemote;

    protected String baseUrlLocal;
    protected String baseUrlRemote;

    @BeforeAll
    void globalSetup() {
        Logs.info("Limpiando carpetas y cargando configuraciones...");

        FileUtil.deleteFolder(new File("reports"));
        FileUtil.deleteFolder(new File("images"));

        localConfig = new ConfigReader("src/main/resources/local.properties");
        remoteConfig = new ConfigReader("src/main/resources/remote.properties");

        extent = ExtentReportManager.createInstance("CrossBrowserSuite");

        headlessLocal = Boolean.parseBoolean(localConfig.getOrDefault("headless", "false"));
        headlessRemote = Boolean.parseBoolean(remoteConfig.getOrDefault("headless", "false"));

        baseUrlLocal = localConfig.get("base.url");
        baseUrlRemote = remoteConfig.get("base.url");

        Logs.info("Configuración global completada.");
    }

    @BeforeEach
    void beforeTest(TestInfo info) {
        if (info.getTestMethod().isPresent() &&
                info.getTestMethod().get().isAnnotationPresent(Disabled.class)) {
            Logs.info("Test deshabilitado: " + info.getDisplayName());
        }
    }

    @AfterEach
    void afterTest() {
        if (driver != null) {
            driver.quit();
            Logs.info("Driver cerrado.");
        }
    }

    @AfterAll
    void globalTearDown() {
        ExtentReportManager.closeReport();
    }

    public boolean isMacOS() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    /**
     * Ejecuta un test en un navegador específico sin grupos ni nodos.
     */
    protected void executeTest(String browserName, BaseDriver driverManager) {
        try {
            test = extent.createTest(browserName.toUpperCase());

            driver = driverManager.createDriver();

            StepsFlow steps = new StepsFlow(
                    driver,
                    browserName.toUpperCase(),
                    test
            );

            steps.executeFlow(baseUrlLocal);

        } catch (Exception e) {
            if (test != null) {
                test.fail("[ERROR] " + e.getMessage());
            }
            Logs.error(e.getMessage());

        } finally {
            if (driver != null) {
                driver.quit();
                Logs.info("Finalizó ejecución en " + browserName.toUpperCase());
            }
        }
    }
}
