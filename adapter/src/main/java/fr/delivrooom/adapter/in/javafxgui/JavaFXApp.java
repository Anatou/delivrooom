package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.application.port.in.GetNameUseCase;
import fr.delivrooom.application.port.in.GuiUseCase;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApp extends javafx.application.Application {

    private static GetNameUseCase getNameUseCase;
    private static GuiUseCase guiUseCase;
    protected static GetNameUseCase getNameUseCase() {
        return getNameUseCase;
    }
    protected static GuiUseCase guiUseCase() {
        return guiUseCase;
    }

    public static void launchGUI(GetNameUseCase getNameUseCase, GuiUseCase guiUseCase) {
        JavaFXApp.getNameUseCase = getNameUseCase;
        JavaFXApp.guiUseCase = guiUseCase;
        javafx.application.Application.launch(JavaFXApp.class);
    }

    @Override
    public void start(Stage stage) {
        MainPane mainPane = new MainPane();

        stage.setTitle("Delivrooom");
        stage.setScene(new Scene(mainPane, 800, 500));
        stage.show();
    }
}

