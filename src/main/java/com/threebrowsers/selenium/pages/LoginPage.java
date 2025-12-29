package com.threebrowsers.selenium.pages;

import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    private final By userInput = By.xpath("//input[contains(@name, 'email')]");
    private final By pswInput = By.xpath("//input[contains(@name, 'password')]");
    private final By logInButton = By.xpath("//button[.//text()[contains(., 'Login')]]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loadPage(String url) {
        driver.get(url);
        Logs.info("Página cargada: " + driver.getTitle());
        waitVisible(By.tagName("body"));
    }

    public void enterUsername(String username) {
        WebElement input = waitVisible(userInput);
        input.clear();
        input.sendKeys(username);
        Logs.info("Nombre de usuario ingresado: " + username);
    }

    public void enterPassword(String password) {
        WebElement input = waitVisible(pswInput);
        input.clear();
        input.sendKeys(password);
        Logs.info("Contraseña ingresada");
    }

    public void clickLogin() {
        safeClick(logInButton);
        Logs.info("Botón de inicio de sesión clickeado.");
    }
}
