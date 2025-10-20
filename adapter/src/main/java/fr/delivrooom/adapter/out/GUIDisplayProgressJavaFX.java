package fr.delivrooom.adapter.out;

import fr.delivrooom.application.port.out.NotifyTSPProgressToGui;

public class GUIDisplayProgressJavaFX implements NotifyTSPProgressToGui {

    @Override
    public void notifyTSPProgressToGui(double percentage) {
        // Join GUI thread and do things
        System.out.println("--- Progress " + percentage + "% ---");
    }
}
