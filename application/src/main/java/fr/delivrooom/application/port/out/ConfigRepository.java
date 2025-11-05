package fr.delivrooom.application.port.out;

import java.util.Properties;

/**
 * Defines the output port for loading configuration properties.
 * This interface is implemented by the adapter layer to provide a mechanism
 * for loading configuration from a specific source (e.g., a .properties file).
 */
public interface ConfigRepository {

    /**
     * Loads the configuration properties.
     *
     * @return A {@link Properties} object containing the loaded configuration.
     */
    Properties loadConfigProperties();
}
