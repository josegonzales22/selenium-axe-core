package com.threebrowsers.selenium.utils;

import com.aventstack.extentreports.ExtentTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AxeUtil {

    private static final String REPORTS_DIR = "reports/axe";
    private static final String AXE_FILES_PATH = "src/main/resources/axe_files/";
    private static final String AXE_JS = AXE_FILES_PATH + "axe.min.js";

    /**
     * Determina el ejecutable correcto según el Sistema Operativo.
     */
    private static String getHtmlReporterPath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return AXE_FILES_PATH + "html_reporter.exe";
        } else {
            return AXE_FILES_PATH + "html_reporter_linux";
        }
    }

    public static void runAccessibilityScan(WebDriver driver, ExtentTest test, String browserName) {
        runAccessibilityScan(driver, test, browserName, null);
    }

    public static void runAccessibilityScan(WebDriver driver, ExtentTest test, String browserName, String pageName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String baseName = (pageName == null || pageName.trim().isEmpty())
                ? "Scan_" + UUID.randomUUID().toString().substring(0, 8)
                : pageName.replaceAll("[^a-zA-Z0-9_-]", "_");

        String finalName = baseName + "_" + timestamp;
        String htmlFileName = finalName + ".html";

        try {
            Path browserReportDir = Path.of(REPORTS_DIR, browserName);
            Files.createDirectories(browserReportDir);

            Path jsonPath = browserReportDir.resolve(finalName + ".json");

            String axeSource = Files.readString(Path.of(AXE_JS));
            ((JavascriptExecutor) driver).executeScript(axeSource);

            String script =
                    "var callback = arguments[arguments.length - 1];" +
                            "axe.run(document, {" +
                            "  restoreScroll: true" +
                            "}).then(results => callback(results));";

            Object rawResults = ((JavascriptExecutor) driver).executeAsyncScript(script);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Map<?, ?> resultMap = (Map<?, ?>) rawResults;
            try (FileWriter file = new FileWriter(jsonPath.toFile())) {
                file.write(gson.toJson(resultMap));
            }

            File reporterFile = new File(getHtmlReporterPath());

            if (!System.getProperty("os.name").toLowerCase().contains("win")) {
                if (reporterFile.exists()) {
                    reporterFile.setExecutable(true);
                }
            }

            ProcessBuilder pb = new ProcessBuilder(
                    reporterFile.getAbsolutePath(),
                    jsonPath.toAbsolutePath().toString(),
                    htmlFileName,
                    browserReportDir.toAbsolutePath().toString()
            );

            pb.redirectError(ProcessBuilder.Redirect.DISCARD);
            pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (Files.exists(jsonPath)) {
                Files.delete(jsonPath);
            }

            String targetPage = (pageName != null ? pageName : "Current Page");

            if (exitCode == 0) {
                String relativePath = "axe/" + browserName + "/" + htmlFileName;
                String linkHtml = "<a href='" + relativePath + "' target='_blank' style='color: #007bff; font-weight: bold;'>[ View Accessibility Report ]</a>";

                assert resultMap != null;
                List<?> violations = (List<?>) resultMap.get("violations");
                if (violations == null || violations.isEmpty()) {
                    test.pass("Axe-Core Scan: No violations found. " + linkHtml);
                } else {
                    test.warning("Axe-Core Scan: Found " + violations.size() + " violations groups. " + linkHtml);
                }
            } else {
                test.fail("Axe-Core Scan: Error in html_reporter (Exit code: " + exitCode + "). Check if binary is compatible with: " + System.getProperty("os.name"));
            }

        } catch (IOException | InterruptedException e) {
            test.fail("Axe-Core Scan: Accessibility analysis error: " + e.getMessage());
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
        }
    }
}