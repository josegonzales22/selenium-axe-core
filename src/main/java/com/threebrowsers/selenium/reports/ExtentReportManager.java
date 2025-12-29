package com.threebrowsers.selenium.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.threebrowsers.selenium.utils.Logs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExtentReportManager {
    private static ExtentReports extent;

    public static ExtentReports createInstance(String browserName) {
        if (extent != null) return extent;

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String safeBrowserName = browserName != null ? browserName.replaceAll("[^a-zA-Z0-9]", "_") : "general";
        String reportPath = "reports/ExecutionReport_" + safeBrowserName + "_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setEncoding("UTF-8");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("Selenium Test Execution - " + safeBrowserName);
        spark.config().setReportName("Cross-Browser Test Results - " + safeBrowserName);
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Tester", "Jose Gonzales");
        extent.setSystemInfo("Browser", safeBrowserName);
        extent.setSystemInfo("Project", "Selenium & Axe Core");

        Logs.info("Reporte creado: " + reportPath);
        return extent;
    }

    public static void closeReport() {
        if (extent != null) {
            extent.flush();
            Logs.info("Reporte cerrado correctamente.");
            extent = null;
        }
    }
}
