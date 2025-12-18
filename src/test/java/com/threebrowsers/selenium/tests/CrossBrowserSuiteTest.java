package com.threebrowsers.selenium.tests;

import java.io.File;
import com.threebrowsers.selenium.drivers.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import com.threebrowsers.selenium.steps.StepsFlow;
import com.threebrowsers.selenium.utils.ConfigReader;
import com.threebrowsers.selenium.utils.FileUtil;
import com.threebrowsers.selenium.reports.ExtentReportManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.TestInfo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CrossBrowserSuiteTest extends BaseTest {

    private static ConfigReader localConfig;
    private static ConfigReader remoteConfig;
    private WebDriver driver;
    private String currentBrowser;

    @BeforeAll
    static void loadConfigs() {
        System.out.println("[INFO] Eliminando carpetas reports e images...");

        FileUtil.deleteFolder(new File("reports"));
        FileUtil.deleteFolder(new File("images"));

        localConfig = new ConfigReader("src/main/resources/local.properties");
        remoteConfig = new ConfigReader("src/main/resources/remote.properties");
        extent = ExtentReportManager.createInstance("CrossBrowserSuite");
        System.out.println("[INFO] Carpetas eliminadas y entorno iniciado.");
    }

    @BeforeEach
    void setupTest(TestInfo testInfo) {
        test = extent.createTest(testInfo.getDisplayName());
        System.out.println("[INFO] Iniciando test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void cleanup() {
        if (driver != null) {
            driver.quit();
            System.out.println("[INFO] Navegador cerrado: " + currentBrowser);
        }
    }

    @AfterAll
    static void tearDown() {
        ExtentReportManager.closeReport();
    }

    @Test
    @Order(1)
    @DisplayName("Chrome local")
    void testInChrome() throws InterruptedException {
        runLocalTest("chrome");
    }
    
    /**/
    @Test
    @Order(2)
    @DisplayName("Edge local")
    void testInEdge() throws InterruptedException {
        runLocalTest("edge");
    }
    /**/

    /**/
    @Test
    @Order(3)
    @DisplayName("Firefox local")
    void testInFirefox() throws InterruptedException {
        runLocalTest("firefox");
    }
    /**/

    /*/
    @Test
    @Order(4)
    @DisplayName("Safari local")
    void testInSafari() throws InterruptedException {
        runLocalTest("safari");
    }
    /**/

    /*/
    @Test
    @Order(5)
    @DisplayName("Safari remoto (VM)")
    void testInSafariVM() throws InterruptedException {
        runRemoteTest();
    }
    /**/
    

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
        currentBrowser = "safari_vm";
        BaseDriver driverManager = new RemoteDriverManager();

        try {
            driver = driverManager.createDriver();
            String baseUrl = remoteConfig.get("base.url");
            StepsFlow steps = new StepsFlow(driver, "safari_vm", test);

            steps.executeFlow(baseUrl);
        } catch (Exception e) {
            test.fail("[ERROR] Error en Safari remoto (VM): " + e.getMessage());
            throw e;
        }
    }
}
