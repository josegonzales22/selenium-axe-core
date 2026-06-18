package com.threebrowsers.selenium.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.threebrowsers.selenium.utils.Logs;

import java.io.File;

public class ExtentReportManager {
    private static final ThreadLocal<ExtentReports> extentThreadLocal = new ThreadLocal<>();

    public static ExtentReports createInstance(String suiteName) {
        if (extentThreadLocal.get() != null) {
            return extentThreadLocal.get();
        }

        String safeSuiteName = suiteName != null ? suiteName.replaceAll("[^a-zA-Z0-9_]", "_") : "general";
        String reportPath = "reports" + File.separator + "ExecutionReport_" + safeSuiteName + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setEncoding("UTF-8");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("QA Automation Framework - " + safeSuiteName);
        spark.config().setReportName("Cross-Browser Regression Results");
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Environment", "CI/CD Pipeline");
        extent.setSystemInfo("Suite", safeSuiteName);
        extent.setSystemInfo("OS", System.getProperty("os.name"));

        extentThreadLocal.set(extent);
        Logs.info("Extent report created in a fixed way in: " + reportPath);

        return extent;
    }

    public static ExtentReports getInstance() {
        return extentThreadLocal.get();
    }

    public static void closeReport() {
        ExtentReports extent = extentThreadLocal.get();
        if (extent != null) {
            try {
                extent.flush();
                Logs.info("Report successfully consolidated and written to disk.");
            } catch (Exception e) {
                Logs.error("Error writing report to disk (Flush): " + e.getMessage());
            } finally {
                extentThreadLocal.remove();
            }
        }
    }
}