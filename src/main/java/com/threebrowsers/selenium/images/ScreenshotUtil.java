package com.threebrowsers.selenium.images;

import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ScreenshotUtil {

    private static final String BASE_DIR = "images";

    private static final ConcurrentHashMap<String, AtomicInteger> browserCounters = new ConcurrentHashMap<>();

    public static void takeScreenshot(WebDriver driver, String browser, String stepName) {
        try {
            String safeBrowser = browser != null ? browser.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase() : "general";

            AtomicInteger counter = browserCounters.computeIfAbsent(safeBrowser, k -> new AtomicInteger(0));
            int orderNumber = counter.incrementAndGet();

            String browserDir = BASE_DIR + File.separator + safeBrowser;
            Files.createDirectories(Paths.get(browserDir));

            String fileName = String.format("%02d_%s.png", orderNumber, stepName);
            String filePath = browserDir + File.separator + fileName;

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), Paths.get(filePath));

            Logs.info("Screenshot guardado: " + filePath);
        } catch (IOException e) {
            Logs.error("No se pudo guardar el screenshot: " + e.getMessage());
        }
    }

    /**
     * Reinicia el contador del navegador indicado.
     * Se debe llamar al iniciar la ejecución de un nuevo navegador.
     */
    public static void resetCounter(String browser) {
        if (browser != null) {
            String safeBrowser = browser.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
            browserCounters.put(safeBrowser, new AtomicInteger(0));
            Logs.info("Contador de screenshots reiniciado para: " + safeBrowser);
        }
    }
}
