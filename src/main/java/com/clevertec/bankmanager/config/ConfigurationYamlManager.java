package com.clevertec.bankmanager.config;

import com.clevertec.bankmanager.shared.exception.configuration.NotFoundConfigurationYamlException;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This enum used as util for get properties from yaml configuration file.
 * Enum values are available globally, and used as a singleton.
 */
public enum ConfigurationYamlManager {
    INSTANCE;

    /** application.yml stores information about project configurations. */
    private static final String CONFIG_YAML_FILE = "/application.yml";
    /** This map stores all yaml properties in application. */
    private final Map<?, ?> property;

    /**
     * Constructor open InputStream for read yaml configuration file, initialize Yaml object from snakeYaml dependency
     * and load all properties in Map.
     */
    ConfigurationYamlManager() {
        try (InputStream is = getClass().getResourceAsStream(CONFIG_YAML_FILE)) {
            Yaml yaml = new Yaml();
            property = (Map<?, ?>) yaml.load(is);
        } catch (IOException e) {
            throw new NotFoundConfigurationYamlException("Can't find yaml configuration file in path: " + CONFIG_YAML_FILE);
        }
    }

    /**
     * Method allows to get property from application.yml file.
     *
     * @param key expected String tag of property in yaml.
     * @return String value of property.
     */
    public String getProperty(String key) {
        Map<?, ?> map = (Map<?, ?>) property.get(key.split("\\.")[0]);
        return map.get(key.split("\\.")[1]).toString();
    }
}
