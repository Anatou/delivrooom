package fr.delivrooom.adapter.out;

import fr.delivrooom.application.port.out.ConfigRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to load configuration from the properties file
 */

/**
 * An adapter that implements the {@link ConfigRepository} port.
 * It loads configuration properties from a {@code .properties} file in the classpath.
 *
 * @param configInputStream The input stream of the configuration file.
 */
public record ConfigLoader(InputStream configInputStream) implements ConfigRepository {

    /**
     * Loads configuration properties from the provided input stream.
     *
     * @return A {@link Properties} object containing the loaded configuration.
     * @throws RuntimeException if the properties file cannot be found or loaded.
     */
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
