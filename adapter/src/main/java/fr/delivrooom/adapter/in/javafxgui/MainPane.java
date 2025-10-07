package fr.delivrooom.adapter.in.javafxgui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MainPane extends VBox {

    public MainPane() {
        super();

        setBackground(Background.fill(Color.LIGHTGREY));
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Name is: ");
        Button button = new Button("Get Name");
        button.setOnAction(e -> {
            String name = JavaFXApp.getNameUseCase().getName();
            label.setText("Name is: " + name);
        });

        HBox topBar = new HBox(10, button, label);

        WebView webView = new WebView();
        VBox.setVgrow(webView, Priority.ALWAYS);

        WebEngine webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                webEngine.executeScript("setTimeout(function(){ map.invalidateSize(); }, 100);");
            }
        });
        webEngine.load(getClass().getResource("/map/map.html").toExternalForm());

        getChildren().addAll(topBar, webView);
    }
}
