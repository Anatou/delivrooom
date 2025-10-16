package fr.delivrooom.adapter.in.javafxgui;


import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import fr.delivrooom.adapter.in.javafxgui.command.AddDeliveryCommand;
import fr.delivrooom.adapter.in.javafxgui.command.CommandManager;
import fr.delivrooom.adapter.in.javafxgui.command.RemoveDeliveryCommand;
import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
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
    //private final CommandManager commandManager = new CommandManager();

    public AppToolBar(AppController controller) {
        super();
        this.controller = controller;

        Button openMapBtn = new Button("Open Map");
        openMapBtn.setOnAction(e -> handleOpenMapFile());

        Button openDemandsBtn = new Button("Open Demands");
        openDemandsBtn.setOnAction(e -> handleOpenDeliveriesFile());

        Button loadDefaultBtn = new Button("Load Default");
        loadDefaultBtn.setOnAction(e -> handleLoadDefault());

        ///ajout/supprimer commande, undo (mettre dans stack les modif)
        Button addDeliveryBtn = new Button("Add Delivery");
        addDeliveryBtn.setOnAction(e -> handleAddDelivery());

        Button removeDeliveryBtn = new Button("Remove Delivery");
        removeDeliveryBtn.setOnAction(e -> handleRemoveDelivery());

        Button undoBtn = new Button("Undo");
        undoBtn.setOnAction(e -> controller.getCommandManager().undo());

        Button redoBtn = new Button("Redo");
        redoBtn.setOnAction(e -> controller.getCommandManager().redo());



        themeToggle = new ToggleButton("🌙");
        themeToggle.setOnAction(e -> handleThemeSwitch());
        themeToggle.setTooltip(new javafx.scene.control.Tooltip("Switch Dark/Light Theme"));

        this.getItems().addAll(openMapBtn, openDemandsBtn, loadDefaultBtn,addDeliveryBtn, removeDeliveryBtn, undoBtn, redoBtn, themeToggle);
        System.out.println("ToolBar items: " + this.getItems());

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


    private void handleAddDelivery() {
        //...
        /*Inter
        Delivery addedDelivery = new Delivery(takeoutIntersection,deliveryIntersection,
        int takeoutDuration, int deliveryDuration);*/

        Intersection takeoutIntersection = new Intersection(99, 45.550404, 4.8744674);
        Intersection deliveryIntersection = new Intersection(100, 45.770404, 4.8744674);
        Delivery addedDelivery = new Delivery(takeoutIntersection,deliveryIntersection,5,5);
        AddDeliveryCommand addDeliveryCommand = new AddDeliveryCommand(controller, addedDelivery);
        controller.getCommandManager().executeCommand(addDeliveryCommand);

    }
    private void handleRemoveDelivery() {

        /*Intersection takeoutIntersection = new Intersection(99, 45.550404, 4.8744674);
        Intersection deliveryIntersection = new Intersection(100, 45.770404, 4.8744674);
        Delivery removedDelivery = new Delivery(takeoutIntersection,deliveryIntersection,5,5);*/
        RemoveDeliveryCommand removeDeliveryCommand = new RemoveDeliveryCommand(controller, controller.getDeliveriesDemand().getDeliveryByIds(99,100));
        controller.getCommandManager().executeCommand(removeDeliveryCommand);
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
