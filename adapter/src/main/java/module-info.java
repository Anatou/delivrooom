module fr.delivrooom.adapter {

    // Exports ports implementations for the bootstrap, and the JavaFX gui to javafx.*
    exports fr.delivrooom.adapter.in.javafxgui to fr.delivrooom.config, javafx.base, javafx.graphics, javafx.controls;
    exports fr.delivrooom.adapter.out to fr.delivrooom.config;

    // Export JavaFX gui to JavaFX

    // Require the application module for the model and ports interfaces
    requires fr.delivrooom.application;

    // JavaFX modules
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;

    // Java modules
    requires java.xml;

    // JavaFX themes, extensions and icons
    requires atlantafx.base;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
}
