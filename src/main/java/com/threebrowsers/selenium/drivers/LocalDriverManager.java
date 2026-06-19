package com.threebrowsers.selenium.drivers;

import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

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
                System.clearProperty("SE_DRIVER_MIRROR_URL");
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
                    Logs.info("Chrome launched in headless mode (Desktop)");
                }

                driver = new ChromeDriver(chromeOptions);
            }

            case "edge" -> {
                System.setProperty("SE_DRIVER_MIRROR_URL", "https://msedgedriver.microsoft.com");
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
                    Logs.info("Edge launched in headless mode (Desktop)");
                }

                driver = new EdgeDriver(edgeOptions);
            }

            case "firefox" -> {
                System.clearProperty("SE_DRIVER_MIRROR_URL");
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                firefoxOptions.addPreference("signon.rememberSignons", false);
                firefoxOptions.addPreference("profile.password_manager_leak_detection", false);

                if (headless) {
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                    Logs.info("Firefox launched in headless mode (Desktop)");
                }

                driver = new FirefoxDriver(firefoxOptions);
                if (!headless) {
                    driver.manage().window().maximize();
                }
            }

            case "safari" -> {
                throw new IllegalStateException("[ERROR] Safari is only available on macOS");
            }

            default -> throw new IllegalArgumentException("[ERROR] Browser not supported: " + browser);
        }

        setupDriver(driver);

        if (!headless) {
            try {
                driver.manage().window().maximize();
            } catch (Exception e) {
                Logs.info("The window could not be maximized graphically (Ignored for CI/CD stability)");
            }
        }
        return driver;
    }
}