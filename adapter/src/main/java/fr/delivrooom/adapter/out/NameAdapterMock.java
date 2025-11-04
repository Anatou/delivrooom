package fr.delivrooom.adapter.out;

import fr.delivrooom.application.port.out.NameRepository;

/**
 * A mock adapter for the {@link NameRepository} port.
 * This class is used for testing and development, providing a hardcoded name.
 */
public class NameAdapterMock implements NameRepository {

    /**
     * Returns a mock name.
     *
     * @return The string "Anatou".
     */
    @Override
    public String getName() {
        return "Anatou";
    }
}
