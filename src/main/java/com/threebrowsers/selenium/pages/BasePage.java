package com.threebrowsers.selenium.pages;

import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Espera hasta que el elemento sea visible y lo devuelve.
     * Mapea el Timeout a un AssertionError para pintar el pipeline de AMARILLO de forma limpia.
     */
    protected WebElement waitVisible(By locator) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(0));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new AssertionError("[QA ERROR] Item did not appear within the stipulated time: " + locator, e);
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    /**
     * Realiza un clic seguro buscando el elemento de forma fresca en cada reintento mediante su Localizador (By).
     */
    protected void safeClick(By locator) {
        int attempts = 0;
        boolean clicked = false;

        while (attempts < 2 && !clicked) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
                element.click();
                clicked = true;
            } catch (Exception e) {
                attempts++;
                if (attempts < 2) {
                    Logs.warning("Normal click failed " + locator + " — Retrying with fresh element...");
                } else {
                    Logs.warning("Attempting to click JS fallback on " + locator + "...");
                    try {
                        WebElement element = driver.findElement(locator);
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                        clicked = true;
                    } catch (Exception jsEx) {
                        throw new AssertionError("[QA ERROR] Interaction blocked. Could not be clicked or accessed via JS: " + locator, jsEx);
                    }
                }
            }
        }
    }

    /**
     * Realiza un clic seguro usando una instancia de WebElement existente (Fallback secundario).
     */
    protected void safeClick(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (Exception e) {
            Logs.warning("Direct click failed. Attempting fallback via JavaScript on the WebElement...");
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            } catch (Exception jsEx) {
                throw new AssertionError("[QA ERROR] The WebElement could not be clicked conventionally or via JS.", jsEx);
            }
        }
    }

    /**
     * Método Genérico para esperar elementos.
     */
    public void waitForElementToLoad(By elementLocator, String pageName) {
        try {
            waitVisible(elementLocator);
            Logs.info("Page element [" + pageName + "] charged correctly.");
        } catch (AssertionError e) {
            Logs.error("The page could not be loaded [" + pageName + "]: " + e.getMessage());
            throw e;
        }
    }
}