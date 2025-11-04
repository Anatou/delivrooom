package fr.delivrooom.application.port.in;

/**
 * Defines the input port for a simple use case that retrieves a name.
 */
public interface GetNameUseCase {
    /**
     * Gets a name.
     *
     * @return A string representing a name.
     */
    String getName();
}
