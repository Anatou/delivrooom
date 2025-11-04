package fr.delivrooom.application.service;

import fr.delivrooom.application.port.in.GetNameUseCase;
import fr.delivrooom.application.port.out.NameRepository;

/**
 * Service for retrieving a name.
 * This class implements the {@link GetNameUseCase} and uses a {@link NameRepository}.
 *
 * @param nameRepository The repository to retrieve the name from.
 */
public record GetNameService(NameRepository nameRepository) implements GetNameUseCase {

    /**
     * Gets a name from the repository.
     *
     * @return A string representing a name.
     */
    @Override
    public String getName() {
        return nameRepository.getName();
    }
}
