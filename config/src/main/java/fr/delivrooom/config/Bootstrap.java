package fr.delivrooom.config;

import fr.delivrooom.adapter.in.InterfaceJavaFX;
import fr.delivrooom.adapter.out.NameAdapterMock;
import fr.delivrooom.application.service.GetNameService;

public class Bootstrap {
    public static void main(String[] args) {

        GetNameService getNameService = new GetNameService(new NameAdapterMock());
        InterfaceJavaFX.launchGUI(getNameService);

    }


}
