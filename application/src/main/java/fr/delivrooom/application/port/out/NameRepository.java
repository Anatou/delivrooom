package fr.delivrooom.application.port.out;

/**
 * Defines the output port for retrieving a name.
 * This interface is implemented by the adapter layer.
 */
public interface NameRepository {
    /**
     * Gets a name.
     *
     * @return A string representing a name.
     */
    String getName();
}
