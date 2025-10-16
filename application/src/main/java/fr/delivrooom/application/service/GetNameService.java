package fr.delivrooom.application.service;

import fr.delivrooom.application.port.in.GetNameUseCase;
import fr.delivrooom.application.port.out.NameRepository;

public record GetNameService(NameRepository nameRepository) implements GetNameUseCase {

    @Override
    public String getName() {
        return nameRepository.getName();
    }
}
