package fr.delivrooom.adapter.in.javafxgui.controller;

import java.io.File;

/**
 * State interface for the State design pattern.
 * Defines the operations that can be performed in different application states.
 */
public interface State {

    /**
     * Handle opening a map file.
     *
     * @param file The map file to open
     */
    void openMapFile(File file);

    /**
     * Handle opening a deliveries demand file.
     *
     * @param file The deliveries file to open
     */
    void openDeliveriesFile(File file);

    /**
     * Get the name of the current state for debugging/logging purposes.
     *
     * @return The state name
     */
    String getStateName();
}
