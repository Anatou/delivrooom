package fr.delivrooom.adapter.in.javafxgui;


import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppToolBar extends ToolBar {

    private final AppController controller;
    private final ToggleButton themeToggle;
    private Stage stage;
    private Scene scene;

    public AppToolBar(AppController controller) {
        super();
        this.controller = controller;

        Button openMapBtn = new Button("Open Map");
        openMapBtn.setOnAction(e -> handleOpenMapFile());

        Button openDemandsBtn = new Button("Open Demands");
        openDemandsBtn.setOnAction(e -> handleOpenDeliveriesFile());

        Button loadDefaultBtn = new Button("Load Default");
        loadDefaultBtn.setOnAction(e -> handleLoadDefault());

        themeToggle = new ToggleButton("🌙");
        themeToggle.setOnAction(e -> handleThemeSwitch());
        themeToggle.setTooltip(new javafx.scene.control.Tooltip("Switch Dark/Light Theme"));

        this.getItems().addAll(openMapBtn, openDemandsBtn, loadDefaultBtn, themeToggle);
    }

    /**
     * Set the stage and scene reference for dialogs and theme switching.
     *
     * @param stage The primary stage
     * @param scene The main scene
     */
    public void setStageAndScene(Stage stage, Scene scene) {
        this.stage = stage;
        this.scene = scene;
    }

    private void handleOpenMapFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            controller.handleOpenMapFile(file);
        }
    }

    private void handleOpenDeliveriesFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Deliveries File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            controller.handleOpenDeliveriesFile(file);
        }
    }

    private void handleLoadDefault() {
        controller.handleLoadDefaultFiles();
    }

    private void handleThemeSwitch() {
        if (scene == null) return;
        if (themeToggle.isSelected()) {
            // Dark theme
            JavaFXApp.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
            themeToggle.setText("☀️");
        } else {
            // Light theme
            JavaFXApp.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            themeToggle.setText("🌙");
        }
    }
}
