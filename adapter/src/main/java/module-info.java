module fr.delivrooom.adapter {


    exports fr.delivrooom.adapter.in.javafxgui;
    exports fr.delivrooom.adapter.out;
    opens fr.delivrooom.adapter.in.javafxgui to javafx.fxml;
    opens fr.delivrooom.adapter.in.javafxgui.controller to javafx.fxml;
    opens fr.delivrooom.adapter.in.javafxgui.command to javafx.fxml;
    opens fr.delivrooom.adapter.in.javafxgui.map to javafx.fxml;
    opens fr.delivrooom.adapter.in.javafxgui.panes to javafx.fxml;
    opens fr.delivrooom.adapter.in.javafxgui.panes.sidebar to javafx.fxml;
    opens fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier to javafx.fxml;
    opens fr.delivrooom.adapter.in.javafxgui.panes.sidebar.delivery to javafx.fxml;

    requires fr.delivrooom.application;

    requires javafx.controls;
    requires java.xml;
    requires javafx.graphics;
    requires javafx.base;
    requires atlantafx.base;
    requires org.controlsfx.controls;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome6;
    requires jdk.jdi;
}
