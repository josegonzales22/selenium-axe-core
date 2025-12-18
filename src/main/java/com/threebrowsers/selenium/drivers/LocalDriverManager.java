package com.threebrowsers.selenium.drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import com.threebrowsers.selenium.drivers.LocalDriverManagerMac;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class LocalDriverManager extends BaseDriver {

    private final String browser;
    private final boolean headless;

    public LocalDriverManager(String browser) {
        this(browser, false);
    }

    public LocalDriverManager(String browser, boolean headless) {
        this.browser = browser.toLowerCase();
        this.headless = headless;
    }

    @Override
    public WebDriver createDriver() {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isMac = os.contains("mac");
        switch (browser) {
            case "chrome" -> {
                if (isMac){
                    WebDriverManager.chromedriver()
                    .setup();
                }else{
                    WebDriverManager.chromedriver()
                        .browserVersion("latest")
                        .setup();
                }
                ChromeOptions chromeOptions = new ChromeOptions();
                java.util.Map<String, Object> prefs = new java.util.HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                chromeOptions.setExperimentalOption("prefs", prefs);
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    System.out.println("[INFO] Chrome ejecutándose en modo headless.");
                }
                driver = new ChromeDriver(chromeOptions);
            }
            case "edge" -> {
                try {
                    URL driverUrl = new URL("https://msedgedriver.microsoft.com/");

                    if (isMac){
                        WebDriverManager.edgedriver()
                        .driverRepositoryUrl(driverUrl)
                        .setup();
                    }else {
                        WebDriverManager.edgedriver()
                            .browserVersion("latest")
                            .driverRepositoryUrl(driverUrl)
                            .setup();
                    }
                } catch (MalformedURLException e) {
                    throw new RuntimeException("[ERROR] URL mal formada para el repositorio del Edge driver.", e);
                }

                EdgeOptions edgeOptions = new EdgeOptions();
                java.util.Map<String, Object> edgePrefs = new java.util.HashMap<>();
                edgePrefs.put("credentials_enable_service", false);
                edgePrefs.put("profile.password_manager_enabled", false);
                edgePrefs.put("profile.password_manager_leak_detection", false);
                edgeOptions.setExperimentalOption("prefs", edgePrefs);
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--disable-gpu");
                    edgeOptions.addArguments("--window-size=1920,1080");
                    System.out.println("[INFO] Edge ejecutándose en modo headless.");
                }
                driver = new EdgeDriver(edgeOptions);
            }
            case "firefox" -> {
                if (isMac){
                    WebDriverManager.firefoxdriver().setup();
                }else{
                    WebDriverManager.firefoxdriver().browserVersion("latest").setup();
                }
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("signon.rememberSignons", false);
                firefoxOptions.addPreference("signon.autofillForms", false);
                firefoxOptions.addPreference("signon.management.page.breach-alerts.enabled", false);
                firefoxOptions.addPreference("profile.password_manager_leak_detection", false);
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                    System.out.println("[INFO] Firefox ejecutándose en modo headless.");
                }
                driver = new FirefoxDriver(firefoxOptions);
            }
            case "safari" -> {
                throw new IllegalStateException("[ERROR] Safari solo está disponible en macOS.");
            }

            default -> throw new IllegalArgumentException("[INFO] Navegador local no soportado: " + browser);
        }
        setupDriver(driver);
        return driver;
    }
}