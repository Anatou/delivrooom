package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;

/**
 * Command to remove a courier from the system.
 * Supports undo by re-adding the courier.
 */
public class CommandRemoveCourier implements Command {

    private final AppController controller;
    private final Courier courier;

    public CommandRemoveCourier(AppController controller, Courier courier) {
        this.controller = controller;
        this.courier = courier;
    }

    @Override
    public void execute() {
        controller.doRemoveCourier(courier);
    }

    @Override
    public void undo() {
        controller.doAddCourier(courier);
    }

    @Override
    public String toString() {
        return "Remove Courier " + courier.getId();
    }
}
