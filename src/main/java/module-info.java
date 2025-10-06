module fr.insalyon.delivrooom {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens fr.insalyon.delivrooom to javafx.fxml;
    exports fr.insalyon.delivrooom;
}