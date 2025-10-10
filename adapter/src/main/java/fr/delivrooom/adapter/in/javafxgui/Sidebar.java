package fr.delivrooom.adapter.in.javafxgui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {
    public Sidebar() {
        super();

        setAlignment(javafx.geometry.Pos.CENTER);
        setPadding(new javafx.geometry.Insets(10));

        Label label = new Label("Name is: ");
        Button button = new Button("Get Name");

        button.setOnAction(e -> {
            String name = JavaFXApp.getNameUseCase().getName();
            label.setText("Name is: " + name);
        });

        getChildren().addAll(label, button);

    }
}
