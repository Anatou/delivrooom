package fr.delivrooom.config;

import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.out.NameAdapterMock;
import fr.delivrooom.adapter.out.XMLCityMapLoader;
import fr.delivrooom.application.port.out.CityMapRepository;
import fr.delivrooom.application.port.out.NameRepository;
import fr.delivrooom.application.service.GetNameService;
import fr.delivrooom.application.service.GuiService;

public class Bootstrap {
    public static void main(String[] args) {

        CityMapRepository cityMapRepository = new XMLCityMapLoader();
        NameRepository nameRepository = new NameAdapterMock();

        GetNameService getNameService = new GetNameService(nameRepository);
        GuiService guiService = new GuiService(cityMapRepository);
        JavaFXApp.launchGUI(getNameService, guiService);

    }


}
