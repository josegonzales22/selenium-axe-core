package com.threebrowsers.selenium.steps;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.threebrowsers.selenium.images.ScreenshotUtil;
import org.openqa.selenium.WebDriver;

public abstract class BaseSteps {
    protected final WebDriver driver;
    protected final String executionIdentifier;
    protected final ExtentTest test;

    public BaseSteps(WebDriver driver, String executionIdentifier, ExtentTest test) {
        this.driver = driver;
        this.executionIdentifier = executionIdentifier;
        this.test = test;
    }

    protected void logStepWithScreenshot(String stepName, String description) {
        String path = ScreenshotUtil.takeScreenshot(driver, executionIdentifier, stepName);
        if (path != null) {
            test.info(description, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } else {
            test.info(description + " (Capture could not be taken due to I/O or driver state)");
        }
    }

    protected void logPassWithScreenshot(String stepName, String description) {
        String path = ScreenshotUtil.takeScreenshot(driver, executionIdentifier, stepName);
        if (path != null) {
            test.pass(description, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } else {
            test.pass(description);
        }
    }

    /**
     * 🚀 INSTANCIACIÓN DINÁMICA: Rompe el acoplamiento estático del ClassLoader
     */
    public void runAccessibilityScan(String pageName) {
        new com.threebrowsers.selenium.utils.AxeUtil().executeScan(this.driver, this.test, this.executionIdentifier, pageName);
    }

    public void runAccessibilityScan() {
        runAccessibilityScan(null);
    }
}