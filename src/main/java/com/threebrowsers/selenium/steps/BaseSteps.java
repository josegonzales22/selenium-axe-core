package com.threebrowsers.selenium.steps;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.threebrowsers.selenium.images.ScreenshotUtil;
import com.threebrowsers.selenium.utils.AxeUtil;
import org.openqa.selenium.WebDriver;

public abstract class BaseSteps {
    protected final WebDriver driver;
    protected final String browserName;
    protected final ExtentTest test;

    public BaseSteps(WebDriver driver, String browserName, ExtentTest test) {
        this.driver = driver;
        this.browserName = browserName;
        this.test = test;
        ScreenshotUtil.resetCounter(browserName);
    }

    protected void runAccessibilityScan(String pageName) {
        AxeUtil.runAccessibilityScan(this.driver, this.test, this.browserName, pageName);
    }

    protected void logStepWithScreenshot(String stepName, String description) {
        String path = ScreenshotUtil.takeScreenshot(driver, browserName, stepName);
        if (path != null) {
            test.info(description, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } else {
            test.info(description + " (Capture could not be taken)");
        }
    }

    protected void logPassWithScreenshot(String stepName, String description) {
        String path = ScreenshotUtil.takeScreenshot(driver, browserName, stepName);
        if (path != null) {
            test.pass(description, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } else {
            test.pass(description);
        }
    }
}