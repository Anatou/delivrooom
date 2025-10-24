package fr.delivrooom.adapter.in.javafxgui.panes;

import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import fr.delivrooom.adapter.in.javafxgui.JavaFXApp;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.adapter.in.javafxgui.controller.StateInitial;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;


import java.io.File;
import java.util.Objects;

public class AppToolBar extends ToolBar {

    private final ToggleSwitch themeToggle;
    private Stage stage;
    private Scene scene;

    private Image logoImgLight;
    private Image logoImgDark;
    private ImageView logo;

    public AppToolBar() {
        super();
        AppController controller = AppController.getController();

        MenuButton open = new MenuButton("Open");

        MenuItem openMapBtn = new MenuItem("Open City Map");
        openMapBtn.setOnAction(e -> handleOpenMapFile());
        MenuItem openDemandsBtn = new MenuItem("Open Delivery Demands");
        openDemandsBtn.disableProperty().bind(controller.stateProperty().map(s -> s instanceof StateInitial));
        openDemandsBtn.setOnAction(e -> handleOpenDeliveriesFile());
        open.getItems().addAll(openMapBtn, openDemandsBtn);

        for (AppController.DefaultMapFilesType type : AppController.DefaultMapFilesType.values()) {
            MenuItem defaultBtn = new MenuItem("Open files \"" + type.name + "\"");
            defaultBtn.setOnAction(e -> handleLoadDefault(type));
            open.getItems().add(defaultBtn);
        }


        Button undoBtn = new Button("", new FontIcon(FontAwesomeSolid.UNDO));
        undoBtn.setOnAction(e -> controller.undoCommand());

        Button redoBtn = new Button("", new FontIcon(FontAwesomeSolid.REDO));
        redoBtn.setOnAction(e -> controller.redoCommand());

        Button saveTourBtn = new Button("", new FontIcon(FontAwesomeSolid.SAVE));
        saveTourBtn.setTooltip(new javafx.scene.control.Tooltip("Save Calculated Tour"));
        saveTourBtn.setOnAction(e -> handleSaveTourFileDialog());

        Button importTourBtn = new Button("", new FontIcon(FontAwesomeSolid.FILE_IMPORT));
        importTourBtn.setTooltip(new javafx.scene.control.Tooltip("Import Calculated Tour"));
       //importTourBtn.setOnAction(e -> controller.requestImportTourFile());


        themeToggle = new ToggleSwitch("");
        themeToggle.setGraphic(new FontIcon(FontAwesomeSolid.MOON));
        themeToggle.selectedProperty().addListener(o -> handleThemeSwitch());
        themeToggle.setTooltip(new javafx.scene.control.Tooltip("Switch Dark/Light Theme"));

        logoImgLight = new Image(Objects.requireNonNull(JavaFXApp.class.getResourceAsStream("/assets/logo_full.png")));
        logoImgDark = new Image(Objects.requireNonNull(JavaFXApp.class.getResourceAsStream("/assets/logo_full_white.png")));
        logo = new ImageView(logoImgLight);
        logo.setFitHeight(25);
        logo.setSmooth(true);
        logo.setPreserveRatio(true);

        // Easter egg: clicking the logo toggles meme mode
        logo.setOnMouseClicked(e -> handleLogoClick());
        logo.setStyle("-fx-cursor: hand;"); // Show it's clickable

        this.getItems().addAll(open, new Spacer(20), undoBtn, redoBtn, new Spacer(10),saveTourBtn, new Spacer(10),importTourBtn, new Spacer(10), themeToggle, new Spacer(), logo, new Spacer(10));
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

    private void handleSaveTourFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Calculated Tour File");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            System.out.println("Fichier sélectionné : " + file.getAbsolutePath());
            AppController.getController().requestSaveTourFile(file);
        }
    }

    private void handleOpenMapFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            AppController.getController().requestOpenMapFile(file);
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
            AppController.getController().requestOpenDeliveriesFile(file);
        }
    }

    private void handleLoadDefault(AppController.DefaultMapFilesType type) {
        AppController.getController().requestLoadDefaultFiles(type);
    }

    private void handleThemeSwitch() {
        if (scene == null) return;
        if (themeToggle.isSelected()) {
            // Dark theme
            JavaFXApp.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
            themeToggle.setGraphic(new FontIcon(FontAwesomeSolid.SUN));
            logo.setImage(logoImgDark);
        } else {
            // Light theme
            JavaFXApp.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            themeToggle.setGraphic(new FontIcon(FontAwesomeSolid.MOON));
            logo.setImage(logoImgLight);
        }
    }

    /**
     * Handle logo click - toggles meme mode (easter egg)
     */
    private void handleLogoClick() {
        AppController controller = AppController.getController();
        controller.toggleMemeMode();
    }
}
