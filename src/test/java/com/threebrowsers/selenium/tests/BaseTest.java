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
    protected Boolean headlessLocal;
    protected Boolean headlessRemote;
    protected String baseUrlLocal;
    protected String baseUrlRemote;

    @BeforeAll
    void globalSetup() {
        String suiteName = this.getClass().getSimpleName().replaceAll("([A-Z])", " $1").trim();
        Logs.info("Starting Suite configurations for: " + suiteName);

        File reportsFolder = new File("reports");
        if (reportsFolder.exists()) {
            FileUtil.deleteFolder(reportsFolder);
        }

        localConfig = new ConfigReader("src/main/resources/local.properties");
        remoteConfig = new ConfigReader("src/main/resources/remote.properties");

        extent = ExtentReportManager.createInstance("CrossBrowserSuite");

        headlessLocal = Boolean.parseBoolean(localConfig.getOrDefault("headless", "false"));
        headlessRemote = Boolean.parseBoolean(remoteConfig.getOrDefault("headless", "false"));

        baseUrlLocal = localConfig.get("base.url");
        baseUrlRemote = remoteConfig.get("base.url");

        suiteParentTest = extent.createTest(suiteName);
        Logs.info("Suite Parent Node successfully created for: " + suiteName);
    }

    @BeforeEach
    void beforeTest(TestInfo info) {
        if (info.getTestMethod().isPresent() &&
                info.getTestMethod().get().isAnnotationPresent(Disabled.class)) {
            Logs.info("Test disabled by annotation: " + info.getDisplayName());
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
     * Ejecuta la prueba de manera aislada y paralela creando un nodo hijo del navegador bajo la Suite actual.
     */
    protected void executeTest(String browserName, BaseDriver driverManager, TestFlowExecutor flowExecutor) {
        ExtentTest browserNode = null;
        WebDriver threadDriver = null;

        // 🚀 CORRECCIÓN CLAVE: Seteamos el identificador para que use exactamente la carpeta visual corta ("chrome_desktop")
        // Esto evitará que ScreenshotUtil falle en sus validaciones internas de retorno de rutas.
        String executionIdentifier = browserName.toLowerCase().replaceAll("[^a-zA-Z0-9_]", "_");

        try {
            browserNode = suiteParentTest.createNode(browserName.toUpperCase());
            threadDriver = driverManager.createDriver();
            com.threebrowsers.selenium.images.ScreenshotUtil.resetCounter(executionIdentifier);

            flowExecutor.execute(threadDriver, browserNode);

        } catch (Throwable e) {
            if (browserNode != null) {
                browserNode.fail("[QA FLOW FAILED] " + e.getMessage());
            }
            Logs.error("[TEST FAILURE] Error in functional flow: " + e.getMessage());

            throw new RuntimeException(e);

        } finally {
            if (threadDriver != null) {
                try {
                    threadDriver.quit();
                } catch (Exception ex) {
                    Logs.warning("Minor error when mitigating and closing the browser process: " + ex.getMessage());
                }
                Logs.info("Execution completed and driver destroyed for: " + browserName.toUpperCase());
            }
        }
    }

    @FunctionalInterface
    public interface TestFlowExecutor {
        void execute(WebDriver driver, ExtentTest test) throws Exception;
    }
}