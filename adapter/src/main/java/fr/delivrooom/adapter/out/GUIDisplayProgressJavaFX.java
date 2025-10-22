package fr.delivrooom.adapter.out;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;
import javafx.application.Platform;

public class GUIDisplayProgressJavaFX implements NotifyTSPProgressToGui {

    @Override
    public void notifyTSPProgressToGui(double progress) {
        if (progress == 0) {
            progress = 0.0001;
        } else if (progress == 1) {
            progress = 0.9999;
        }
        double finalProgress = progress;

        AppController.getController().tourCalculationProgressProperty().set(finalProgress);

    }
}
