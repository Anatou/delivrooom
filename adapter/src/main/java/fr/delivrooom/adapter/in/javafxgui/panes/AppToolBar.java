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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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

    private final Image logoImgLight;
    private final Image logoImgDark;
    private final ImageView logo;

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

        MenuButton edit = new MenuButton("Edit");

        MenuItem undoBtn = new MenuItem("Undo", new FontIcon(FontAwesomeSolid.UNDO));
        undoBtn.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.META_DOWN));
        undoBtn.setOnAction(e -> controller.undoCommand());

        MenuItem redoBtn = new MenuItem("Redo", new FontIcon(FontAwesomeSolid.REDO));
        redoBtn.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.META_DOWN));
        redoBtn.setOnAction(e -> controller.redoCommand());

        edit.setOnShowing(e -> {
            String undoName = controller.getNextUndoCommandName();
            undoBtn.setDisable(undoName == null);
            if (undoName == null) {
                undoBtn.setText("Undo");
            } else {
                undoBtn.setText("Undo (" + undoName + ")");
            }
            String redoName = controller.getNextRedoCommandName();
            redoBtn.setDisable(redoName == null);
            if (redoName == null) {
                redoBtn.setText("Redo");
            } else {
                redoBtn.setText("Redo (" + redoName + ")");
            }
        });
        Button saveTourBtn = new Button("", new FontIcon(FontAwesomeSolid.SAVE));
        saveTourBtn.setTooltip(new javafx.scene.control.Tooltip("Save Calculated Tour"));
        saveTourBtn.setOnAction(e -> handleSaveTourFileDialog());
        saveTourBtn.disableProperty().bind(controller.tourCalculatedProperty().not());


        Button importTourBtn = new Button("", new FontIcon(FontAwesomeSolid.FILE_IMPORT));
        importTourBtn.setTooltip(new javafx.scene.control.Tooltip("Import Calculated Tour"));
        importTourBtn.setOnAction(e -> handleLoadTourFileDialog());

        edit.getItems().addAll(undoBtn, redoBtn);

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
        logo.setOnMouseClicked(e -> AppController.getController().toggleMemeMode());

        this.getItems().addAll(open, edit, new Spacer(10),saveTourBtn, new Spacer(10),importTourBtn, new Spacer(10), themeToggle, new Spacer(), logo, new Spacer(10));
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
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers binaires", "*.bin")
        );

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            System.out.println("Fichier sélectionné : " + file.getAbsolutePath());
            AppController.getController().requestSaveTourFile(file);
        }
    }

    private void handleLoadTourFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Calculated Tour File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichiers binaires", "*.bin")
        );

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            System.out.println("Fichier sélectionné : " + file.getAbsolutePath());
            AppController.getController().requestLoadTourSolution(file);
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
}
