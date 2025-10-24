package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;

/**
 * Command to add a courier to the system.
 * Supports undo by removing the courier.
 */
public class CommandAddCourier implements Command {

    private final AppController controller;
    private final Courier courier;

    public CommandAddCourier(AppController controller, Courier courier) {
        this.controller = controller;
        this.courier = courier;
    }

    @Override
    public void execute() {
        controller.doAddCourier(courier);
    }

    @Override
    public void undo() {
        controller.doRemoveCourier(courier);
    }

    @Override
    public String toString() {
        return "Add Courier " + courier.getId();
    }
}
