package fr.delivrooom.adapter.in.javafxgui;


import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

public class AppToolBar extends ToolBar {

    private final AppController controller;
    private final ToggleSwitch themeToggle;
    private Stage stage;
    private Scene scene;

    public AppToolBar(AppController controller) {
        super();
        this.controller = controller;

        MenuButton open = new MenuButton("Open");

        MenuItem openMapBtn = new MenuItem("Open Map");
        openMapBtn.setOnAction(e -> handleOpenMapFile());
        MenuItem openDemandsBtn = new MenuItem("Open Demands");
        openDemandsBtn.setOnAction(e -> handleOpenDeliveriesFile());
        MenuItem loadDefaultBtn = new MenuItem("Open Default Map and Demands");
        loadDefaultBtn.setOnAction(e -> handleLoadDefault());

        open.getItems().addAll(openMapBtn, openDemandsBtn, loadDefaultBtn);

        Button undoBtn = new Button("", new FontIcon(FontAwesomeSolid.UNDO));
        undoBtn.setOnAction(e -> controller.getCommandManager().undo());

        Button redoBtn = new Button("", new FontIcon(FontAwesomeSolid.REDO));
        redoBtn.setOnAction(e -> controller.getCommandManager().redo());


        themeToggle = new ToggleSwitch("");
        themeToggle.setGraphic(new FontIcon(FontAwesomeSolid.MOON));
        themeToggle.selectedProperty().addListener(o -> handleThemeSwitch());
        themeToggle.setTooltip(new javafx.scene.control.Tooltip("Switch Dark/Light Theme"));

        this.getItems().addAll(open, new Spacer(20), undoBtn, redoBtn, new Spacer(), themeToggle, new Spacer(10));

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
            themeToggle.setGraphic(new FontIcon(FontAwesomeSolid.SUN));
        } else {
            // Light theme
            JavaFXApp.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            themeToggle.setGraphic(new FontIcon(FontAwesomeSolid.MOON));
        }
    }
}
