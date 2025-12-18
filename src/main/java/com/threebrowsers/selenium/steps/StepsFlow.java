package com.threebrowsers.selenium.steps;

import com.threebrowsers.selenium.pages.*;
import com.threebrowsers.selenium.utils.AxeUtil;
import org.openqa.selenium.WebDriver;
import com.threebrowsers.selenium.images.ScreenshotUtil;
import com.aventstack.extentreports.ExtentTest;

import java.util.Set;

public class StepsFlow {

    private final WebDriver driver;
    private final String browserName;
    private final LoginPage loginPage;
    private final NavigatePage navigatePage;
    private final ExtentTest test;

    public StepsFlow(WebDriver driver, String browserName, ExtentTest test) {
        this.driver = driver;
        this.loginPage = new LoginPage(driver);
        this.navigatePage = new NavigatePage(driver);
        this.browserName = browserName;
        this.test = test;
    }

    public void executeFlow(String baseUrl) throws InterruptedException {
        String username = "zoaib@zoaibkhan.com";
        String psw = "testing123";
        ScreenshotUtil.resetCounter(browserName);

        test.info("Cargando página de login");
        loginPage.loadPage(baseUrl);
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, browserName, "loginPage_loaded");
        AxeUtil.runAccessibilityScan(driver, test, browserName, "1_loginPage");

        test.info("Ingresando nombre de usuario");
        loginPage.enterUsername(username);
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, browserName, "username_entered");

        test.info("Ingresando contraseña");
        loginPage.enterPassword(psw);
        Thread.sleep(1000);
        ScreenshotUtil.takeScreenshot(driver, browserName, "system_selected");

        test.info("Haciendo clic en login");
        loginPage.clickLogin();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, browserName, "login_clicked");

        test.info("Cerrando menu");
        navigatePage.clickMenu();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, browserName, "menu_closed");
        AxeUtil.runAccessibilityScan(driver, test, browserName, "2_mainPage");

        test.info("Ingresando a página de componentes");
        navigatePage.goToComponentsPage();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, browserName, "components_page");
        AxeUtil.runAccessibilityScan(driver, test, browserName, "3_componentsPage");

        test.info("Ingresando a página de formulario");
        navigatePage.goToFormsPage();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, browserName, "forms_page");
        AxeUtil.runAccessibilityScan(driver, test, browserName, "4_formsPage");

        test.info("Ingresando a página de contenido");
        navigatePage.goToContentPage();
        Thread.sleep(2000);
        ScreenshotUtil.takeScreenshot(driver, browserName, "content_page");
        AxeUtil.runAccessibilityScan(driver, test, browserName, "5_contentPage");

        test.pass("Flujo completado exitosamente en " + browserName.toUpperCase());
    }
}
