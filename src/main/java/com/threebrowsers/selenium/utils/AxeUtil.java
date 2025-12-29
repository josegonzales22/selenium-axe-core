package com.threebrowsers.selenium.utils;

import com.aventstack.extentreports.ExtentTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class AxeUtil {

    private static final String REPORTS_DIR = "reports/axe";
    private static final String NODE_AXE_PATH = "nodejs/node_modules/axe-core/axe.min.js";
    private static final String NODE_GENERATOR = "nodejs/generate-html.js";

    public static void runAccessibilityScan(WebDriver driver, ExtentTest test, String browserName, String pageName) {
        Path artifactPath = Path.of("artifacts");
        String safePageName = pageName.replaceAll("[^a-zA-Z0-9_-]", "_");

        try {
            test.info("Ejecutando escaneo de accesibilidad con axe-core en: " + pageName);

            Path browserReportDir = Path.of(REPORTS_DIR, browserName);
            Files.createDirectories(browserReportDir);
            Path jsonPath = browserReportDir.resolve(safePageName + ".json");
            Files.createDirectories(artifactPath);

            String axeSource = Files.readString(Path.of(NODE_AXE_PATH));
            ((JavascriptExecutor) driver).executeScript(axeSource);

            String script =
                    "var callback = arguments[arguments.length - 1];" +
                            "axe.run(document, {" +
                            "  runOnly: { type: 'tag', values: ['wcag2a', 'wcag2aa', 'wcag21a', 'wcag21aa'] }" +
                            "}).then(results => callback(results));";

            Object rawResults = ((JavascriptExecutor) driver).executeAsyncScript(script);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Map<?, ?> resultMap = (Map<?, ?>) rawResults;
            try (FileWriter file = new FileWriter(jsonPath.toFile())) {
                file.write(gson.toJson(resultMap));
            }

            // Lógica de generación de HTML
            ProcessBuilder pb = new ProcessBuilder("node", NODE_GENERATOR, jsonPath.toString(), safePageName);
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                Path generated = artifactPath.resolve("accessibilityReport.html");
                Path finalHtmlPath = browserReportDir.resolve(safePageName + ".html");
                Files.move(generated, finalHtmlPath, StandardCopyOption.REPLACE_EXISTING);

                String relativePath = "axe/" + browserName + "/" + safePageName + ".html";
                String linkHtml = "<a href='" + relativePath + "' target='_blank' style='color: #007bff; font-weight: bold;'>[ Ver Reporte de Accesibilidad ]</a>";

                List<?> violations = (List<?>) resultMap.get("violations");
                if (violations == null || violations.isEmpty()) {
                    test.pass("✅ No se encontraron violaciones en " + pageName + ". " + linkHtml);
                } else {
                    test.warning("❌ Se encontraron " + violations.size() + " violaciones en " + pageName + ". " + linkHtml);
                }
            } else {
                test.fail("❌ Error al generar reporte HTML para " + pageName);
            }

        } catch (IOException | InterruptedException e) {
            test.fail("Error en análisis de accesibilidad: " + e.getMessage());
        } finally {
            cleanupArtifacts(artifactPath);
        }
    }

    private static void cleanupArtifacts(Path artifactPath) {
        try {
            if (Files.exists(artifactPath)) {
                try (var stream = Files.list(artifactPath)) {
                    if (stream.findAny().isEmpty()) {
                        Files.delete(artifactPath);
                        Logs.info("Carpeta 'artifacts' eliminada.");
                    }
                }
            }
        } catch (IOException e) {
            Logs.error("No se pudo limpiar la carpeta artifacts: " + e.getMessage());
        }
    }
}