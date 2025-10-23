package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Delivery;

/**
 * Command to add a delivery to the deliveries demand.
 * Supports undo by removing the delivery.
 */
public record CommandAddDelivery(AppController controller, Delivery delivery) implements Command {

    @Override
    public void execute() {
        controller.doAddDelivery(delivery);
    }

    @Override
    public void undo() {
        controller.doRemoveDelivery(delivery);
    }
}

