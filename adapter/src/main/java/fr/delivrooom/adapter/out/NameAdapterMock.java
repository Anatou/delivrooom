package fr.delivrooom.adapter.out;

import fr.delivrooom.application.port.out.NameRepository;

public class NameAdapterMock implements NameRepository {

    @Override
    public String getName() {
        return "Anatou";
    }
}
