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
     */
    protected WebElement waitVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new AssertionError("[ERROR] Elemento no visible: " + locator, e);
        }
    }

    /**
     * Realiza un clic seguro con reintento automático y fallback por JS (usando By).
     */
    protected void safeClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        safeClick(element);
    }

    /**
     * Realiza un clic seguro con reintento automático y fallback por JS (usando WebElement).
     */
    protected void safeClick(WebElement element) {
        int attempts = 0;
        boolean clicked = false;

        while (attempts < 2 && !clicked) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                clicked = true;
            } catch (Exception e) {
                attempts++;
                if (attempts < 2) {
                    Logs.warning("Falló clic normal en elemento — Reintentando...");
                } else {
                    Logs.warning("Intentando clic JS fallback en elemento...");
                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                        clicked = true;
                    } catch (Exception jsEx) {
                        throw new AssertionError("[ERROR] No se pudo hacer clic ni por JS en elemento.", jsEx);
                    }
                }
            }
        }
    }

    /**
     * Método Genérico para esperar elementos
     *
     * @param elementLocator El By (xpath, id, etc) que se debe esperar
     * @param pageName       Nombre descriptivo para el log
     */
    public void waitForElementToLoad(By elementLocator, String pageName) {
        try {
            waitVisible(elementLocator);
            Logs.info("[INFO] Elemento de la página [" + pageName + "] cargado correctamente.");
        } catch (Exception e) {
            Logs.error("[ERROR] No se pudo cargar la página [" + pageName + "]: " + e.getMessage());
        }
    }
}
