package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;

/**
 * Command to assign a courier to a delivery.
 * Supports undo by restoring the previous courier assignment.
 */
public class CommandAssignCourier implements Command {

    private final AppController controller;
    private final Delivery delivery;
    private final Courier newCourier;
    private Courier previousCourier;

    public CommandAssignCourier(AppController controller, Delivery delivery, Courier newCourier) {
        this.controller = controller;
        this.delivery = delivery;
        this.newCourier = newCourier;
    }

    @Override
    public void execute() {
        previousCourier = controller.getCourierForDelivery(delivery);
        controller.doAssignCourier(delivery, newCourier);
    }

    @Override
    public void undo() {
        controller.doAssignCourier(delivery, previousCourier);
    }
}
