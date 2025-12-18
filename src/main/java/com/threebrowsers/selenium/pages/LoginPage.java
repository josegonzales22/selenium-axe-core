package com.threebrowsers.selenium.pages;

import org.openqa.selenium.*;

public class LoginPage extends BasePage {
    private final By userInput = By.xpath("//input[contains(@name, 'email')]");
    private final By pswInput = By.xpath("//input[contains(@name, 'password')]");
    private final By logInButton = By.xpath("//button[.//text()[contains(., 'Login')]]");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void loadPage(String url) {
        driver.get(url);
        System.out.println("[INFO] Página cargada: " + driver.getTitle());
        waitVisible(By.tagName("body"));
    }

    public void enterUsername(String username) {
        WebElement input = waitVisible(userInput);
        input.clear();
        input.sendKeys(username);
        System.out.println("[INFO] Nombre de usuario ingresado: " + username);
    }

    public void enterPassword(String password) {
        WebElement input = waitVisible(pswInput);
        input.clear();
        input.sendKeys(password);
        System.out.println("[INFO] Contraseña ingresada");
    }

    public void clickLogin() {
        safeClick(logInButton);
        System.out.println("[INFO] Botón de inicio de sesión clickeado.");
    }
}
