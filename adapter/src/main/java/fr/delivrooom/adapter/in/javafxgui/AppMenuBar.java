package fr.delivrooom.adapter.in.javafxgui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;

public class AppMenuBar extends MenuBar {

    public AppMenuBar() {
        super();

        Menu fileMenu = getFileMenu();
        Menu editMenu = getEditMenu();

        getMenus().addAll(fileMenu, editMenu);
    }

    private Menu getFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem openMapMenu = new MenuItem("Open map file");
        openMapMenu.setAccelerator(new KeyCharacterCombination("O", KeyCharacterCombination.META_DOWN));
        MenuItem openDemandsMenu = new MenuItem("Open demands file");
        openDemandsMenu.setAccelerator(new KeyCharacterCombination("O", KeyCharacterCombination.SHIFT_DOWN, KeyCombination.META_DOWN));

        fileMenu.getItems().addAll(openMapMenu, openDemandsMenu);
        return fileMenu;
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
