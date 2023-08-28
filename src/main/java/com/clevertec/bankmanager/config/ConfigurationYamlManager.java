package com.clevertec.bankmanager.config;

import com.clevertec.bankmanager.shared.exception.configuration.NotFoundConfigurationYamlException;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public enum ConfigurationYamlManager {
    INSTANCE;

    private static final String CONFIG_YAML_FILE = "/application.yml";
    private final Map<?, ?> property;

    ConfigurationYamlManager() {

        try (InputStream is = getClass().getResourceAsStream(CONFIG_YAML_FILE)) {
            Yaml yaml = new Yaml();
            property = (Map<?, ?>) yaml.load(is);
        } catch (IOException e) {
            throw new NotFoundConfigurationYamlException("Can't find yaml configuration file in path: " + CONFIG_YAML_FILE);
        }
    }

    public String getProperty(String key) {
        Map<?, ?> map = (Map<?, ?>) property.get(key.split("\\.")[0]);
        return map.get(key.split("\\.")[1]).toString();
    }
}
