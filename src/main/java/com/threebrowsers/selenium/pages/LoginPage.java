package com.threebrowsers.selenium.pages;

import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class LoginPage extends BasePage {
    private final By userInput = By.xpath("//input[contains(@name, 'email')]");
    private final By pswInput = By.xpath("//input[contains(@name, 'password')]");
    private final By logInButton = By.xpath("//button[.//text()[contains(., 'Login')]]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loadPage(String url) {
        driver.get(url);
        Logs.info("Page loaded in browser. Validating initial DOM...");
        waitVisible(By.tagName("body"));
        waitForElementToLoad(userInput, "Login Page");
    }

    public void enterUsername(String username) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(userInput));
            input.clear();
            input.sendKeys(username);
            Logs.info("Username entered successfully.");
        } catch (Exception e) {
            throw new AssertionError("[QA ERROR] The user field did not accept interaction: " + userInput, e);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    public void enterPassword(String password) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(pswInput));
            input.clear();
            input.sendKeys(password);
            Logs.info("Password entered successfully.");
        } catch (Exception e) {
            throw new AssertionError("[QA ERROR] The password field did not accept interaction: " + pswInput, e);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    public void clickLogin() {
        safeClick(logInButton);
        Logs.info("Login button clicked.");
    }
}