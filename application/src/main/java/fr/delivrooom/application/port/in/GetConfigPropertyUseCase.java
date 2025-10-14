package fr.delivrooom.application.port.in;

public interface GetConfigPropertyUseCase {
    /**
     * Get a property value by key
     *
     * @param key the property key
     * @return the property value, or null if not found
     */
    String getProperty(String key);

    /**
     * Get a property value by key with a default value
     *
     * @param key          the property key
     * @param defaultValue the default value if key is not found
     * @return the property value, or defaultValue if not found
     */
    String getProperty(String key, String defaultValue);

    /**
     * Get an integer property value
     *
     * @param key          the property key
     * @param defaultValue the default value if key is not found or cannot be parsed
     * @return the property value as integer
     */
    int getIntProperty(String key, int defaultValue);
}
