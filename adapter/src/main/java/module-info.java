module fr.delivrooom.adapter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    exports fr.delivrooom.adapter.out;
    exports fr.delivrooom.adapter.in.javafxgui;
    opens fr.delivrooom.adapter.in.javafxgui to javafx.fxml;
    opens fr.delivrooom.adapter.out to javafx.fxml;
    exports fr.delivrooom.adapter.in.javafxgui.controller;
    opens fr.delivrooom.adapter.in.javafxgui.controller to javafx.fxml;

    requires fr.delivrooom.application;
    requires java.xml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.web;
    requires java.desktop;
    requires atlantafx.base;


    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
}
