package fr.delivrooom.adapter.out;

import fr.delivrooom.application.port.out.ConfigRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to load configuration from properties file
 */
public class ConfigLoader implements ConfigRepository {

    private final InputStream configInputStream;

    public ConfigLoader(InputStream configInputStream) {
        this.configInputStream = configInputStream;
    }

    public Properties loadConfigProperties() {
        Properties properties = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties file not found in resources. Please create a copy of config.properties.template and customize it.");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration", ex);
        }
        return properties;
    }
}
