package fr.delivrooom.application.port.out;

public interface NotifyTSPProgressToGui {
    /**
     * Notifies the GUI of the progress of a TSP calculation.
     * This method is intended to be called by a TSP solver to provide real-time
     * feedback to the user, for example, by updating a progress bar.
     *
     * @param progress A value between 0.0 and 1.0 representing the completion percentage.
     */
    void notifyTSPProgressToGui(double progress);
}
