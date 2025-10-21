package fr.delivrooom.adapter.in.javafxgui.panes.sidebar;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome6.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.application.Platform;


/**
 * Bottom fixed section with progress bar and GO button.
 * The progress bar is shown when loading, and the GO button is shown when not loading.
 */
public class BottomSection extends VBox {

    private final ProgressBar progressBar;
    private final Label progressLabel; // Label to show progress percentage
    private final Button goButton;

    public BottomSection() {
        super(10);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        AppController controller = AppController.getController();

        // Progress bar (shown when loading)
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setProgress(0); // Initial progress
        progressBar.visibleProperty().bind(controller.tourBeingCalculatedBinding());
        progressBar.managedProperty().bind(progressBar.visibleProperty());

        // Progress label (percentage)
        progressLabel = new Label("0%"); // Initial text
        progressLabel.visibleProperty().bind(controller.tourBeingCalculatedBinding());
        progressLabel.managedProperty().bind(progressLabel.visibleProperty());

        // GO button (shown when not loading)
        goButton = new Button("GO");
        goButton.setGraphic(new FontIcon(FontAwesomeSolid.PLAY));
        goButton.setMaxWidth(Double.MAX_VALUE);
        goButton.getStyleClass().addAll("accent", "large");
        goButton.visibleProperty().bind(controller.tourBeingCalculatedBinding().not());

        goButton.setOnAction(e -> controller.handleCalculateTour());
        goButton.managedProperty().bind(goButton.visibleProperty());

        getChildren().addAll(progressBar, progressLabel, goButton);

        // Bind progress bar and label to notifyTSPprogression
        controller.tourCalculationProgressProperty().addListener((obs, oldValue, newValue) -> {
            updateProgress(newValue.doubleValue());
        });

    }

    public void updateProgress(double progress) {
        Platform.runLater(() -> {
            System.out.println("Mise à jour du progress : " + progress); // Log pour vérifier la valeur
            progressBar.setProgress(progress);
            progressLabel.setText(String.format("%.0f%%", progress * 100)); // Met à jour le pourcentage
        });
    }



}
