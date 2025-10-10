package fr.delivrooom.adapter.in.javafxgui;

import atlantafx.base.theme.PrimerLight;
import fr.delivrooom.application.port.in.GetConfigPropertyUseCase;
import fr.delivrooom.application.port.in.GetNameUseCase;
import fr.delivrooom.application.port.in.GuiUseCase;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApp extends javafx.application.Application {

    private static GetNameUseCase getNameUseCase;
    private static GuiUseCase guiUseCase;
    private static GetConfigPropertyUseCase getConfigPropertyUseCase;


    public static void launchGUI(GetNameUseCase getNameUseCase, GuiUseCase guiUseCase, GetConfigPropertyUseCase getConfigPropertyUseCase) {
        JavaFXApp.getNameUseCase = getNameUseCase;
        JavaFXApp.guiUseCase = guiUseCase;
        JavaFXApp.getConfigPropertyUseCase = getConfigPropertyUseCase;
        javafx.application.Application.launch(JavaFXApp.class);
    }

    protected static GetNameUseCase getNameUseCase() {
        return getNameUseCase;
    }

    public static GuiUseCase guiUseCase() {
        return guiUseCase;
    }

    protected static GetConfigPropertyUseCase getConfigPropertyUseCase() {
        return getConfigPropertyUseCase;
    }

    @Override
    public void start(Stage stage) {
        MainPane mainPane = new MainPane();

        // Set the stage reference for file chooser dialogs
        mainPane.setStage(stage);

        setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        stage.setTitle("Delivrooom");
        stage.setScene(new Scene(mainPane, 800, 500));
        stage.show();
    }
}

