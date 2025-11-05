package fr.delivrooom.config;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.out.*;
import fr.delivrooom.application.port.out.*;
import fr.delivrooom.application.service.ConfigService;
import fr.delivrooom.application.service.GetNameService;
import fr.delivrooom.application.service.GuiService;
import fr.delivrooom.application.service.TourCalculatorService;

import java.io.IOException;
import java.io.InputStream;

public class Bootstrap {
    /**
     * The main entry point of the application.
     * This method sets up the dependency injection framework, instantiating repositories,
     * services (use cases), and launching the JavaFX GUI.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {

        ConfigRepository configRepository;
        try (InputStream configInputStream = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (configInputStream == null) {
                throw new RuntimeException("config.properties file not found in resources. Please create a copy of config.properties and customize it.");
            }
            //System.out.println("Loading configuration from config.properties...");
            configRepository = new ConfigLoader(configInputStream);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration", ex);
        }

        NameRepository nameRepository = new NameAdapterMock();
        CityMapRepository cityMapRepository = new XMLCityMapLoader();
        DeliveriesRepository deliveriesRepository = new XMLDeliveriesLoader();
        NotifyTSPProgressToGui notifyTSPProgressToGui = new GUIDisplayProgressJavaFX();

        GetNameService getNameService = new GetNameService(nameRepository);
        GuiService guiService = new GuiService(cityMapRepository, deliveriesRepository);
        ConfigService configService = new ConfigService(configRepository);
        TourCalculatorService tourCalculatorService = new TourCalculatorService(notifyTSPProgressToGui);

        JavaFXApp.launchGUI(getNameService, guiService, configService, tourCalculatorService);

    }
}
