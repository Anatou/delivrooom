module fr.delivrooom.application {
    requires java.desktop;
    requires javafx.graphics;
    // Adapters should only see the model classes along with the port interfaces
    exports fr.delivrooom.application.model;
    exports fr.delivrooom.application.port.in;
    exports fr.delivrooom.application.port.out;

    // Services should be available to use only by the bootstrap
    exports fr.delivrooom.application.service to fr.delivrooom.config;
}
