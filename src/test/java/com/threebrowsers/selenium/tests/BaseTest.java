package com.threebrowsers.selenium.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.threebrowsers.selenium.reports.ExtentReportManager;
import com.threebrowsers.selenium.utils.ConfigReader;
import com.threebrowsers.selenium.utils.FileUtil;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import java.io.File;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    protected ExtentReports extent;
    protected ConfigReader localConfig;
    protected ConfigReader remoteConfig;

    protected ExtentTest test;
    protected WebDriver driver;

    @BeforeAll
    void globalSetup() {
        System.out.println("[INFO] Limpiando carpetas y cargando configuraciones...");

        FileUtil.deleteFolder(new File("reports"));
        FileUtil.deleteFolder(new File("images"));

        localConfig = new ConfigReader("src/main/resources/local.properties");
        remoteConfig = new ConfigReader("src/main/resources/remote.properties");

        extent = ExtentReportManager.createInstance("CrossBrowserSuite");

        System.out.println("[INFO] Configuración global completada.");
    }

    @BeforeEach
    void setupTest(TestInfo info) {
        test = extent.createTest(info.getDisplayName());
        System.out.println("[INFO] Iniciando test: " + info.getDisplayName());
    }

    @AfterEach
    void tearDownTest() {
        if (driver != null) {
            driver.quit();
            System.out.println("[INFO] Driver cerrado.");
        }
    }

    @AfterAll
    void globalTearDown() {
        ExtentReportManager.closeReport();
        System.out.println("[INFO] Reporte cerrado correctamente.");
    }
}
