package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainPane extends BorderPane {

    private final AppController controller;
    private final AppToolBar toolBar;

    public MainPane() {
        super();

        // Create the controller
        controller = new AppController();

        // Create UI components
        MapCanvas canvas = new MapCanvas();
        Sidebar sidebar = new Sidebar();
        toolBar = new AppToolBar(controller);

        // Wire the controller to the canvas
        controller.setMapCanvas(canvas);

        // Set up the layout
        setTop(toolBar);
        setCenter(new SplitPane(sidebar, canvas));
    }

    /**
     * Set the stage and scene reference for dialogs and theme switching.
     *
     * @param stage The primary stage
     * @param scene The main scene
     */
    public void setStageAndScene(Stage stage, Scene scene) {
        toolBar.setStageAndScene(stage, scene);
    }
}
