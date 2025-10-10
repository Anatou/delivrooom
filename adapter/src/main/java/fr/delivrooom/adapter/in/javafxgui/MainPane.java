package fr.delivrooom.adapter.in.javafxgui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class MainPane extends BorderPane {

    public MainPane() {
        super();

        setPadding(new Insets(20));
        setBackground(Background.fill(Color.LIGHTGREY));


        MapCanvas canvas = new MapCanvas();
        setCenter(canvas);
    }
}
