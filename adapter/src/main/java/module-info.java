module fr.delivrooom.adapter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens fr.delivrooom.adapter.in to javafx.fxml;

    exports fr.delivrooom.adapter.in;
    exports fr.delivrooom.adapter.out;

    requires fr.delivrooom.application;
    requires java.xml;
}
