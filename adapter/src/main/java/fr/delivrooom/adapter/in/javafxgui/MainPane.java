package fr.delivrooom.adapter.in.javafxgui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainPane extends VBox {

    public MainPane() {
        super();

        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setBackground(Background.fill(Color.LIGHTGREY));

        Label label = new Label("Name is: ");
        Button button = new Button("Get Name");

        button.setOnAction(e -> {
            String name = JavaFXApp.getNameUseCase().getName();
            label.setText("Name is: " + name);
        });


        MapCanvas canvas = new MapCanvas();
        Pane canvasPane = new Pane(canvas);
        VBox.setVgrow(canvasPane, Priority.ALWAYS);
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        getChildren().addAll(button, label, canvasPane);
    }
}
