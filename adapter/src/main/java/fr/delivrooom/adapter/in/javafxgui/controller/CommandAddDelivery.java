package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Delivery;

public record CommandAddDelivery(AppController controller, Delivery delivery) implements Command {

    @Override
    public void execute() {

        controller.addDelivery(delivery);

    }

    @Override
    public void undo() {
        controller.removeDelivery(delivery);

    }


}

