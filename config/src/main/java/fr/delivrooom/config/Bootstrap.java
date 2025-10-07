package fr.delivrooom.config;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.out.ConfigLoader;
import fr.delivrooom.adapter.out.NameAdapterMock;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.adapter.out.XMLDeliveriesLoader;
import fr.delivrooom.application.port.out.CityMapRepository;
import fr.delivrooom.application.port.out.ConfigRepository;
import fr.delivrooom.application.port.out.DeliveriesRepository;
import fr.delivrooom.application.port.out.NameRepository;
import fr.delivrooom.application.service.ConfigService;
import fr.delivrooom.application.service.GetNameService;
import fr.delivrooom.application.service.GuiService;

public class Bootstrap {
    public static void main(String[] args) {

        NameRepository nameRepository = new NameAdapterMock();
        ConfigRepository configRepository = new ConfigLoader();
        CityMapRepository cityMapRepository = new XMLCityMapLoader();
        DeliveriesRepository deliveriesRepository = new XMLDeliveriesLoader();


        GetNameService getNameService = new GetNameService(nameRepository);
        GuiService guiService = new GuiService(cityMapRepository, deliveriesRepository);
        ConfigService configService = new ConfigService(configRepository);

        JavaFXApp.launchGUI(getNameService, guiService, configService);

    }


}
