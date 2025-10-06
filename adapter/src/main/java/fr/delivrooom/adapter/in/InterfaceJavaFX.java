package fr.delivrooom.adapter.in;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import fr.delivrooom.application.port.in.GetNameUseCase;

public class InterfaceJavaFX extends Application {

    private static GetNameUseCase getNameUseCase;

    public static void launchGUI(GetNameUseCase getNameUseCase) {
        InterfaceJavaFX.getNameUseCase = getNameUseCase;
        Application.launch(InterfaceJavaFX.class);
    }

    Label label = new Label("Name is: ");
    Button button = new Button("Get Name");

    @Override
    public void start(Stage stage) {

        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setSpacing(20);

        button.setOnAction(e -> {
            String name = getNameUseCase.getName();
            label.setText("Name is: "+name);
        });

        root.getChildren().add(button);
        root.getChildren().add(label);


        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("Hello!");
        stage.show();
    }
}

