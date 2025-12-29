package com.threebrowsers.selenium.pages;

import com.threebrowsers.selenium.utils.Logs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        Logs.info("Interacción con botón de menú");
    }

    public void goToComponentsPage() {
        safeClick(componentButtonPage);
        waitForElementToLoad(componentButtonPage, "Componentes");
    }

    public void goToFormsPage() {
        safeClick(formButtonPage);
        waitForElementToLoad(formButtonPage, "Formularios");
    }

    public void goToContentPage() {
        safeClick(contentButtonPage);
        waitForElementToLoad(contentButtonPage, "Contenido");
    }
}