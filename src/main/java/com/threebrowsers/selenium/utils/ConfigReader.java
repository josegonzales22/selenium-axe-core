package com.threebrowsers.selenium.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private final Properties props = new Properties();

    public ConfigReader(String path) {
        try (FileInputStream input = new FileInputStream(path)) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo de configuración: " + path, e);
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
