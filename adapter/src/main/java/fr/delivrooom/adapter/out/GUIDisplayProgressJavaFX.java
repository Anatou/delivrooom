package fr.delivrooom.adapter.out;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;
import javafx.application.Platform;

/**
 * An adapter that implements the {@link NotifyTSPProgressToGui} port.
 * It updates a JavaFX progress property on the UI thread, allowing for safe
 * progress updates from background TSP calculation threads.
 */
public class GUIDisplayProgressJavaFX implements NotifyTSPProgressToGui {

    @Override
    public void notifyTSPProgressToGui(double progress) {
        if (progress == 0) {
            progress = 0.0001;
        } else if (progress == 1) {
            progress = 0.9999;
        }
        double finalProgress = progress;

        Platform.runLater(() -> AppController.getController().tourCalculationProgressProperty().set(finalProgress));

    }
}
