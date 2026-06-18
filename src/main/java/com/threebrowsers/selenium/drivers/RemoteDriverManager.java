package com.threebrowsers.selenium.drivers;

import com.threebrowsers.selenium.utils.ConfigReader;
import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RemoteDriverManager extends BaseDriver {

    public RemoteDriverManager() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public WebDriver createDriver() {
        ConfigReader config = new ConfigReader("src/main/resources/remote.properties");

        Map<String, Object> rawCapabilitiesMap = new HashMap<>();

        for (String key : config.getProperties().stringPropertyNames()) {
            if (key.startsWith("capabilities.")) {
                String value = config.get(key);
                String fullPath = key.substring("capabilities.".length());

                buildDynamicCapabilities(rawCapabilitiesMap, fullPath, convertValue(value));
            }
        }

        MutableCapabilities caps = new MutableCapabilities();
        rawCapabilitiesMap.forEach(caps::setCapability);
        String hubUrl = config.getOrDefault("remote.hub.url", "http://localhost:4444/wd/hub");

        try {
            Logs.info("Connecting to the Remote Grid in: " + hubUrl);
            driver = new RemoteWebDriver(new URL(hubUrl), caps);
        } catch (Exception e) {
            Logs.error("CRITICAL INFRASTRUCTURE FAILURE: RemoteWebDriver could not be instantiated: " + e.getMessage());
            throw new RuntimeException("[CI/CD INFRASTRUCTURE ERROR] Remote grid inaccessible: ", e);
        }

        setupDriver(driver);

        boolean isRemoteHeadless = config.getProperties().toString().toLowerCase().contains("headless");

        if (!isRemoteHeadless) {
            try {
                Logs.info("Maximizing a remote window securely...");
                driver.manage().window().maximize();
            } catch (Exception e) {
                Logs.warning("The window could not be maximized in the remote environment (Ignored for stability): " + e.getMessage());
            }
        } else {
            Logs.info("Remote session detected as Headless. Relying on the resolution assigned by capabilities.");
        }

        return driver;
    }

    /**
     * Reconstruye de forma agnóstica la estructura de mapas.
     * Convierte inteligentemente cualquier formato '_Options' u '_options'
     * a ':options' para cumplir con W3C.
     */
    @SuppressWarnings("unchecked")
    private void buildDynamicCapabilities(Map<String, Object> currentMap, String path, Object value) {
        String normalizedPath = path.replaceAll("(?i)_options", ":options");
        String[] parts = normalizedPath.split("\\.");

        if (parts.length == 1) {
            currentMap.put(parts[0], value);
            return;
        }

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            currentMap = (Map<String, Object>) currentMap.computeIfAbsent(
                    part,
                    k -> new HashMap<String, Object>()
            );
        }

        currentMap.put(parts[parts.length - 1], value);
    }

    private Object convertValue(String value) {
        if (value == null) return null;

        if (value.equalsIgnoreCase("true")) return true;
        if (value.equalsIgnoreCase("false")) return false;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }
}