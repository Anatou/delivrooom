package fr.insalyon.delivrooom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    Label label = new Label("Hello from JavaFX Application!");
    Button button = new Button("Say 'Hello World'");

    @Override
    public void start(Stage stage) throws IOException {


        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setSpacing(20);

        button.setOnAction(e -> {
            root.getChildren().remove(label);
            root.getChildren().addFirst(label);
        });

        root.getChildren().add(button);


        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("Hello!");
        stage.show();
    }
}
