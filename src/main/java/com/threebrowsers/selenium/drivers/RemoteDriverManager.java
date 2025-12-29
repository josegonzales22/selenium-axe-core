package com.threebrowsers.selenium.drivers;

import com.threebrowsers.selenium.utils.ConfigReader;
import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RemoteDriverManager extends BaseDriver {

    @Override
    public WebDriver createDriver() {
        ConfigReader config = new ConfigReader("src/main/resources/remote.properties");

        String remoteUrl = config.get("remote.url");
        String browser = config.getOrDefault("browser", "chrome");

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", browser);

        try {
            // 🔹 LambdaTest
            if (remoteUrl.contains("lambdatest")) {

                caps.setCapability("browserVersion",
                        config.getOrDefault("browser.version", "latest"));
                caps.setCapability("platformName",
                        config.getOrDefault("platform.name", "macOS Ventura"));

                Map<String, Object> ltOptions = new HashMap<>();
                ltOptions.put("user", config.get("remote.user"));
                ltOptions.put("accessKey", config.get("remote.key"));
                ltOptions.put("project", config.getOrDefault("project.name", "LambdaTest Project"));
                ltOptions.put("build", config.getOrDefault("build.name", "Build 1"));
                ltOptions.put("name", config.getOrDefault("test.name", "Test"));
                ltOptions.put("video", true);
                ltOptions.put("console", true);
                ltOptions.put("network", true);

                caps.setCapability("LT:Options", ltOptions);

            }
            // 🔹 BrowserStack (tu soporte actual)
            else if (remoteUrl.contains("browserstack")) {

                Map<String, Object> bsOptions = new HashMap<>();
                bsOptions.put("userName", config.get("remote.user"));
                bsOptions.put("accessKey", config.get("remote.key"));
                bsOptions.put("projectName", config.getOrDefault("project.name", "BrowserStack Demo"));
                bsOptions.put("os", config.getOrDefault("os", "OS X"));
                bsOptions.put("osVersion", config.getOrDefault("os.version", "Sonoma"));

                caps.setCapability("bstack:options", bsOptions);

            } else {
                Logs.info("Ejecutando en Grid local o VM: " + remoteUrl);
            }

            driver = new RemoteWebDriver(new URL(remoteUrl), caps);

        } catch (MalformedURLException e) {
            throw new RuntimeException("URL remota inválida: " + remoteUrl, e);
        }

        setupDriver(driver);
        return driver;
    }
}
