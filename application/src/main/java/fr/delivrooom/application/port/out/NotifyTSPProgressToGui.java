package fr.delivrooom.application.port.out;

public interface NotifyTSPProgressToGui {
    /**
     * TSP calculation progress notification to GUI
     *
     * @param progress progress value between 0 and 1
     */
    void notifyTSPProgressToGui(double progress);
}
