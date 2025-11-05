package fr.delivrooom.adapter.in.javafxgui;

import atlantafx.base.theme.PrimerLight;
import fr.delivrooom.adapter.in.javafxgui.panes.MainPane;
import fr.delivrooom.application.port.in.CalculateTourUseCase;
import fr.delivrooom.application.port.in.GetConfigPropertyUseCase;
import fr.delivrooom.application.port.in.GetNameUseCase;
import fr.delivrooom.application.port.in.GuiUseCase;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main entry point for the JavaFX application.
 * This class initializes and launches the user interface. It holds static references
 * to the application's use cases, making them accessible throughout the GUI components.
 */
public class JavaFXApp extends javafx.application.Application {

    private static GetNameUseCase getNameUseCase;
    private static GuiUseCase guiUseCase;
    private static GetConfigPropertyUseCase getConfigPropertyUseCase;
    private static CalculateTourUseCase calculateTourUseCase;

    /**
     * Initializes the use cases and launches the JavaFX application.
     * This method is called by the Bootstrap to inject the application services.
     *
     * @param getNameUseCase           The use case for getting names.
     * @param guiUseCase               The use case for GUI-related operations.
     * @param getConfigPropertyUseCase The use case for accessing configuration properties.
     * @param calculateTourUseCase     The use case for tour calculation.
     */
    public static void launchGUI(GetNameUseCase getNameUseCase, GuiUseCase guiUseCase, GetConfigPropertyUseCase getConfigPropertyUseCase, CalculateTourUseCase calculateTourUseCase) {
        JavaFXApp.getNameUseCase = getNameUseCase;
        JavaFXApp.guiUseCase = guiUseCase;
        JavaFXApp.getConfigPropertyUseCase = getConfigPropertyUseCase;
        JavaFXApp.calculateTourUseCase = calculateTourUseCase;
        javafx.application.Application.launch(JavaFXApp.class);
    }

    /**
     * @return The GetNameUseCase instance.
     */
    public static GetNameUseCase getNameUseCase() {
        return getNameUseCase;
    }

    /**
     * @return The GuiUseCase instance.
     */
    public static GuiUseCase guiUseCase() {
        return guiUseCase;
    }

    /**
     * @return The GetConfigPropertyUseCase instance.
     */
    public static GetConfigPropertyUseCase getConfigPropertyUseCase() {
        return getConfigPropertyUseCase;
    }

    /**
     * @return The CalculateTourUseCase instance.
     */
    public static CalculateTourUseCase getCalculateTourUseCase() {
        return calculateTourUseCase;
    }


    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     */
    @Override
    public void start(Stage stage) {
        MainPane mainPane = new MainPane();

        setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        stage.setTitle("Delivrooom");
        stage.setScene(new Scene(mainPane, 1300, 750));
        mainPane.setStageAndScene(stage, stage.getScene());
        stage.getIcons().add(new Image(Objects.requireNonNull(JavaFXApp.class.getResourceAsStream("/assets/logo_mini.png"))));
        stage.show();
    }
}

