package com.threebrowsers.selenium.pages;

import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class NavigatePage extends BasePage {
    private final By menuButton = By.xpath("//button[.//text()[contains(., 'menu')]]");
    private final By componentButtonPage = By.xpath("(//a[contains(@href, '/components')])[1]");
    private final By formButtonPage = By.xpath("(//a[contains(@href, '/forms')])[1]");
    private final By contentButtonPage = By.xpath("(//a[contains(@href, '/content')])[1]");

    public NavigatePage(WebDriver driver) {
        super(driver);
    }

    public void clickMenu() {
        safeClick(menuButton);
        Logs.info("Menu button interaction successfully executed");
    }

    public void goToComponentsPage() {
        safeClick(componentButtonPage);
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
            wait.until(ExpectedConditions.urlContains("/components"));
            Logs.info("Successful redirection to the Components page.");
        } catch (Exception e) {
            throw new AssertionError("[QA ERROR] Transition to the Components page was not completed. Current URL: " + driver.getCurrentUrl(), e);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    public void goToFormsPage() {
        safeClick(formButtonPage);

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
            wait.until(ExpectedConditions.urlContains("/forms"));
            Logs.info("Successful redirection to the Forms page.");
        } catch (Exception e) {
            throw new AssertionError("[QA ERROR] Transition to the Forms page was not completed. Current URL: " + driver.getCurrentUrl(), e);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    public void goToContentPage() {
        safeClick(contentButtonPage);

        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
            wait.until(ExpectedConditions.urlContains("/content"));
            Logs.info("Successful redirection to the Content page.");
        } catch (Exception e) {
            throw new AssertionError("[QA ERROR] Transition to the Content page was not completed. Current URL: " + driver.getCurrentUrl(), e);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }
}