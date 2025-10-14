package fr.delivrooom.application.service;

import fr.delivrooom.application.port.in.GetNameUseCase;
import fr.delivrooom.application.port.out.NameRepository;

public class GetNameService implements GetNameUseCase {

    public GetNameService(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    private final NameRepository nameRepository;

    @Override
    public String getName() {
        return nameRepository.getName();
    }
}
