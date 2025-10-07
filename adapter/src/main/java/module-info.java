module fr.delivrooom.adapter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    exports fr.delivrooom.adapter.out;
    exports fr.delivrooom.adapter.in.javafxgui;
    opens fr.delivrooom.adapter.in.javafxgui to javafx.fxml;

    requires fr.delivrooom.application;
    requires java.xml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.web;
}
