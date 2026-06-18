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
            throw new RuntimeException("[CI/CD CONFIG ERROR] Properties file missing or inaccessible in path: " + path, e);
        }
    }

    /**
     * Busca la clave primero en las propiedades del sistema (-Dkey),
     * luego en las variables de entorno, y si no existe, recurre al archivo físico .properties.
     */
    public String get(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.isEmpty()) {
            return systemProperty;
        }

        String envVariable = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envVariable != null && !envVariable.isEmpty()) {
            return envVariable;
        }

        return props.getProperty(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    public Properties getProperties() {
        return this.props;
    }
}