package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;

/**
 * Command to assign a courier to a delivery.
 * Supports undo by restoring the previous courier assignment.
 */
public class CommandAssignCourier implements Command {

    private final AppController controller;
    private final Delivery delivery;
    private final Intersection store;
    private final Courier newCourier;
    private Courier previousCourier;

    public CommandAssignCourier(AppController controller, Delivery delivery, Intersection store, Courier newCourier) {
        this.controller = controller;
        this.store = store;
        this.delivery = delivery;
        this.newCourier = newCourier;
    }

    @Override
    public void execute() {
        previousCourier = controller.getCourierForDelivery(delivery);
        controller.doAssignCourier(delivery, store, newCourier);
    }

    @Override
    public void undo() {
        controller.doAssignCourier(delivery, store, previousCourier);
    }

    @Override
    public String toString() {
        if (newCourier == null) {
            return "Remove Courier assignment for " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
        }
        return "Assign Courier " + newCourier.getId() + " to " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
    }
}
