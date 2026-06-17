package com.threebrowsers.selenium.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.threebrowsers.selenium.drivers.BaseDriver;
import com.threebrowsers.selenium.reports.ExtentReportManager;
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

    protected ExtentTest suiteParentTest;
    protected WebDriver driver;

    protected Boolean headlessLocal;
    protected Boolean headlessRemote;

    protected String baseUrlLocal;
    protected String baseUrlRemote;

    @BeforeAll
    void globalSetup() {
        Logs.info("Cleaning folders and loading settings...");

        FileUtil.deleteFolder(new File("reports"));
        FileUtil.deleteFolder(new File("images"));

        localConfig = new ConfigReader("src/main/resources/local.properties");
        remoteConfig = new ConfigReader("src/main/resources/remote.properties");

        extent = ExtentReportManager.createInstance("CrossBrowserSuite");

        headlessLocal = Boolean.parseBoolean(localConfig.getOrDefault("headless", "false"));
        headlessRemote = Boolean.parseBoolean(remoteConfig.getOrDefault("headless", "false"));

        baseUrlLocal = localConfig.get("base.url");
        baseUrlRemote = remoteConfig.get("base.url");

        String suiteName = this.getClass().getSimpleName().replaceAll("([A-Z])", " $1").trim();
        suiteParentTest = extent.createTest(suiteName);

        Logs.info("Global configuration completed for: " + suiteName);
    }

    @BeforeEach
    void beforeTest(TestInfo info) {
        if (info.getTestMethod().isPresent() &&
                info.getTestMethod().get().isAnnotationPresent(Disabled.class)) {
            Logs.info("Test disabled: " + info.getDisplayName());
        }
    }

    @AfterEach
    void afterTest() {
        if (driver != null) {
            driver.quit();
            Logs.info("Driver closed.");
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
     * Ejecuta la prueba creando un nodo hijo del navegador bajo la Suite actual.
     */
    protected void executeTest(String browserName, BaseDriver driverManager, TestFlowExecutor flowExecutor) {
        ExtentTest browserNode = null;
        try {
            browserNode = suiteParentTest.createNode(browserName.toUpperCase());
            driver = driverManager.createDriver();

            flowExecutor.execute(driver, browserNode);

        } catch (Exception e) {
            if (browserNode != null) {
                browserNode.fail("[ERROR] " + e.getMessage());
            }
            Logs.error(e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
                Logs.info("Execution ended in " + browserName.toUpperCase());
            }
        }
    }

    @FunctionalInterface
    public interface TestFlowExecutor {
        void execute(WebDriver driver, ExtentTest test) throws Exception;
    }
}