package fr.delivrooom.config;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.out.NameAdapterMock;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.adapter.out.XMLDeliveriesLoader;
import fr.delivrooom.application.port.out.CityMapRepository;
import fr.delivrooom.application.port.out.DeliveriesRepository;
import fr.delivrooom.application.port.out.NameRepository;
import fr.delivrooom.application.service.GetNameService;
import fr.delivrooom.application.service.GuiService;

public class Bootstrap {
    public static void main(String[] args) {

        CityMapRepository cityMapRepository = new XMLCityMapLoader();
        System.out.println("CityMap loaded.");
        DeliveriesRepository deliveriesRepository = new XMLDeliveriesLoader();
        System.out.println("Deliveries loaded.");

        NameRepository nameRepository = new NameAdapterMock();

        GetNameService getNameService = new GetNameService(nameRepository);
        GuiService guiService = new GuiService(cityMapRepository, deliveriesRepository);
        JavaFXApp.launchGUI(getNameService, guiService);

    }


}
