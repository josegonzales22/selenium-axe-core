package com.threebrowsers.selenium.drivers;

import com.threebrowsers.selenium.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class RemoteDriverManager extends BaseDriver {

    @Override
    public WebDriver createDriver() {
        ConfigReader config = new ConfigReader("src/main/resources/remote.properties");

        String remoteUrl = config.get("remote.url"); // obligatorio
        String browser = config.getOrDefault("browser", "chrome");
        String user = config.getOrDefault("remote.user", "");
        String key = config.getOrDefault("remote.key", "");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setBrowserName(browser);

        try {
            if (!user.isEmpty() && !key.isEmpty()) {
                Map<String, Object> serviceOptions = new HashMap<>();
                serviceOptions.put("userName", user);
                serviceOptions.put("accessKey", key);
                serviceOptions.put("projectName", config.getOrDefault("project.name", "Selenium Tests"));
                serviceOptions.put("os", config.getOrDefault("os", "ANY"));
                serviceOptions.put("osVersion", config.getOrDefault("os.version", ""));
                caps.setCapability("bstack:options", serviceOptions);
            } else {
                System.out.println("[INFO] Ejecutando en Grid local o VM: " + remoteUrl);
            }

            driver = new RemoteWebDriver(new URL(remoteUrl), caps);
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL remota inválida: " + remoteUrl, e);
        }

        setupDriver(driver);
        return driver;
    }
}
