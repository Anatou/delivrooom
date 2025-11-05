package fr.delivrooom.adapter.in.javafxgui.panes;

import fr.delivrooom.adapter.in.javafxgui.map.MapCanvas;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main container for the application's user interface.
 * It uses a {@link BorderPane} layout to organize the main components:
 * {@link AppToolBar} at the top, and a {@link SplitPane} in the center containing
 * the {@link Sidebar} and {@link MapCanvas}.
 */
public class MainPane extends BorderPane {

    private final AppToolBar toolBar;

    /**
     * Constructs the MainPane, initializing and arranging all primary UI components.
     */
    public MainPane() {
        super();
        // Create UI components
        MapCanvas canvas = new MapCanvas();
        Sidebar sidebar = new Sidebar();
        toolBar = new AppToolBar();

        // Set up the layout
        setTop(toolBar);
        SplitPane splitPane = new SplitPane(sidebar, canvas);
        splitPane.setDividerPositions(0.25);
        setCenter(splitPane);
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
