package com.threebrowsers.selenium.drivers;

import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.util.HashMap;
import java.util.Map;

public class LocalDriverManager extends BaseDriver {

    private final String browser;
    protected final boolean headless;

    public LocalDriverManager(String browser, boolean headless) {
        this.browser = browser.toLowerCase();
        this.headless = headless;
    }

    @Override
    public WebDriver createDriver() {
        switch (browser) {
            case "chrome" -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                
                chromeOptions.setExperimentalOption("prefs", prefs);
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");

                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    Logs.info("Chrome lanzado en modo Headless (Desktop)");
                }

                driver = new ChromeDriver(chromeOptions);
            }

            case "edge" -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                
                Map<String, Object> edgePrefs = new HashMap<>();
                edgePrefs.put("credentials_enable_service", false);
                edgePrefs.put("profile.password_manager_enabled", false);
                
                edgeOptions.setExperimentalOption("prefs", edgePrefs);
                edgeOptions.addArguments("--start-maximized");

                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--disable-gpu");
                    edgeOptions.addArguments("--window-size=1920,1080");
                    Logs.info("Edge lanzado en modo Headless (Desktop)");
                }

                driver = new EdgeDriver(edgeOptions);
            }

            case "firefox" -> {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                
                firefoxOptions.addPreference("signon.rememberSignons", false);
                firefoxOptions.addPreference("profile.password_manager_leak_detection", false);

                if (headless) {
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                    Logs.info("Firefox lanzado en modo Headless (Desktop)");
                }

                driver = new FirefoxDriver(firefoxOptions);
                if (!headless) {
                    driver.manage().window().maximize();
                }
            }

            case "safari" -> {
                throw new IllegalStateException("[ERROR] Safari solo está disponible en macOS.");
            }

            default -> throw new IllegalArgumentException("[ERROR] Navegador no soportado: " + browser);
        }

        setupDriver(driver);
        return driver;
    }
}