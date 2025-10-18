module fr.delivrooom.adapter {

    requires org.controlsfx.controls;

    exports fr.delivrooom.adapter.out;
    exports fr.delivrooom.adapter.in.javafxgui;
    opens fr.delivrooom.adapter.in.javafxgui to javafx.fxml;
    opens fr.delivrooom.adapter.out to javafx.fxml;
    exports fr.delivrooom.adapter.in.javafxgui.controller;
    opens fr.delivrooom.adapter.in.javafxgui.controller to javafx.fxml;
    exports fr.delivrooom.adapter.in.javafxgui.command;
    opens fr.delivrooom.adapter.in.javafxgui.command to javafx.fxml;
    exports fr.delivrooom.adapter.in.javafxgui.map;
    opens fr.delivrooom.adapter.in.javafxgui.map to javafx.fxml;
    exports fr.delivrooom.adapter.in.javafxgui.panes;
    opens fr.delivrooom.adapter.in.javafxgui.panes to javafx.fxml;

    requires fr.delivrooom.application;
    requires javafx.controls;
    requires java.xml;
    requires javafx.graphics;
    requires javafx.base;
    requires atlantafx.base;


    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
    requires jdk.jdi;
}
