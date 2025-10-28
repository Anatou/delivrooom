package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Delivery;

/**
 * Command to remove a delivery from the deliveries demand.
 * Supports undo by re-adding the delivery.
 */
public record CommandRemoveDelivery(AppController controller, Delivery delivery) implements Command {

    @Override
    public void execute() {
        controller.doRemoveDelivery(delivery);
    }

    @Override
    public void undo() {
        controller.doAddDelivery(delivery);
    }

    @Override
    public String toString() {
        return "Remove Delivery " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
    }
}

