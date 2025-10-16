package fr.delivrooom.adapter.in.javafxgui.controller;

import java.net.URL;

/**
 * State interface for the State design pattern.
 * Defines the operations that can be performed in different application states.
 */
public interface State {

    /**
     * Handle opening a map file.
     *
     * @param url The map file url to open
     */
    void openMapFile(URL url);

    /**
     * Handle opening a deliveries demand file.
     *
     * @param url The deliveries file url to open
     */
    void openDeliveriesFile(URL url);

    /**
     * Get the name of the current state for debugging/logging purposes.
     *
     * @return The state name
     */
    String getStateName();
}
