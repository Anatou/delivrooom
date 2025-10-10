package fr.delivrooom.adapter.in.javafxgui;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane {

    public MainPane() {
        super();

        AppMenuBar menuBar = new AppMenuBar();
        setTop(menuBar);

        MapCanvas canvas = new MapCanvas();
        Sidebar sidebar = new Sidebar();

        setCenter(new SplitPane(sidebar, canvas));
    }
}
