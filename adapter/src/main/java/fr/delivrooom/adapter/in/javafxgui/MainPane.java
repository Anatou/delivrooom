package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainPane extends BorderPane {

    private final AppController controller;
    private final AppMenuBar menuBar;

    public MainPane() {
        super();

        // Create the controller
        controller = new AppController();

        // Create UI components
        MapCanvas canvas = new MapCanvas();
        Sidebar sidebar = new Sidebar();
        menuBar = new AppMenuBar(controller);

        // Wire the controller to the canvas
        controller.setMapCanvas(canvas);

        // Set up the layout
        setTop(menuBar);
        setCenter(new SplitPane(sidebar, canvas));
    }

    /**
     * Set the stage reference for file chooser dialogs.
     * This should be called after the MainPane is added to a Scene.
     *
     * @param stage The primary stage
     */
    public void setStage(Stage stage) {
        menuBar.setStage(stage);
    }
}
