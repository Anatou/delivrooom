package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppMenuBar extends MenuBar {

    private final AppController controller;
    private Stage stage;

    public AppMenuBar(AppController controller) {
        super();
        this.controller = controller;

        Menu fileMenu = getFileMenu();
        Menu editMenu = getEditMenu();

        getMenus().addAll(fileMenu, editMenu);
    }

    /**
     * Set the stage reference for file chooser dialogs.
     *
     * @param stage The primary stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Menu getFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem openMapMenu = new MenuItem("Open map file");
        openMapMenu.setAccelerator(new KeyCharacterCombination("O", KeyCharacterCombination.META_DOWN));
        openMapMenu.setOnAction(e -> handleOpenMapFile());

        MenuItem openDemandsMenu = new MenuItem("Open demands file");
        openDemandsMenu.setAccelerator(new KeyCharacterCombination("O", KeyCharacterCombination.SHIFT_DOWN, KeyCombination.META_DOWN));
        openDemandsMenu.setOnAction(e -> handleOpenDeliveriesFile());

        fileMenu.getItems().addAll(openMapMenu, openDemandsMenu);
        return fileMenu;
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

    private Menu getEditMenu() {
        Menu editMenu = new Menu("Edit");

        MenuItem undoMenu = new MenuItem("Undo");
        undoMenu.setAccelerator(new KeyCharacterCombination("Z", KeyCharacterCombination.META_DOWN));
        MenuItem redoMenu = new MenuItem("Redo");
        redoMenu.setAccelerator(new KeyCharacterCombination("Y", KeyCharacterCombination.META_DOWN));

        editMenu.getItems().addAll(undoMenu, redoMenu);
        return editMenu;
    }

}
