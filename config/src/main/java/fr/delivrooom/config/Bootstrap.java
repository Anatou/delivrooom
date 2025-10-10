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

import java.io.IOException;
import java.io.InputStream;

public class Bootstrap {
    public static void main(String[] args) {

        ConfigRepository configRepository;
        try (InputStream configInputStream = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (configInputStream == null) {
                throw new RuntimeException("config.properties file not found in resources. Please create a copy of config.properties and customize it.");
            }
            configRepository = new ConfigLoader(configInputStream);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration", ex);
        }

        NameRepository nameRepository = new NameAdapterMock();
        CityMapRepository cityMapRepository = new XMLCityMapLoader();
        DeliveriesRepository deliveriesRepository = new XMLDeliveriesLoader();


        GetNameService getNameService = new GetNameService(nameRepository);
        GuiService guiService = new GuiService(cityMapRepository, deliveriesRepository);
        ConfigService configService = new ConfigService(configRepository);

        JavaFXApp.launchGUI(getNameService, guiService, configService);

    }
}
