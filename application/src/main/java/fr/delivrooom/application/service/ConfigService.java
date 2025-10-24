package fr.delivrooom.application.service;

import fr.delivrooom.application.port.in.GetConfigPropertyUseCase;
import fr.delivrooom.application.port.out.ConfigRepository;

import java.util.Properties;

public class ConfigService implements GetConfigPropertyUseCase {

    private final Properties configProperties;

    public ConfigService(ConfigRepository configRepository) {
        this.configProperties = configRepository.loadConfigProperties();
    }


    /**
     * Get a property value by key
     *
     * @param key the property key
     * @return the property value, or null if not found
     */
    @Override
    public String getProperty(String key) {
        return configProperties.getProperty(key);
    }

    /**
     * Get a property value by key with a default value
     *
     * @param key          the property key
     * @param defaultValue the default value if key is not found
     * @return the property value, or defaultValue if not found
     */
    @Override
    public String getProperty(String key, String defaultValue) {
        return configProperties.getProperty(key, defaultValue);
    }

    /**
     * Get an integer property value
     *
     * @param key          the property key
     * @param defaultValue the default value if key is not found or cannot be parsed
     * @return the property value as integer
     */
    @Override
    public int getIntProperty(String key, int defaultValue) {
        String value = configProperties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer value for property " + key + ": " + value);
            return defaultValue;
        }
    }

    @Override
    public double getDoubleProperty(String key, double defaultValue) {
        String value = configProperties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid double value for property " + key + ": " + value);
            return defaultValue;
        }
    }
}
