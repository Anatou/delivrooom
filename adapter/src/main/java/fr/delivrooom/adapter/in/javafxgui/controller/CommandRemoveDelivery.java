package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Delivery;

public record CommandRemoveDelivery(AppController controller, Delivery delivery) implements Command {

    @Override
    public void execute() {
        controller.removeDelivery(delivery);
    }

    @Override
    public void undo() {
        controller.addDelivery(delivery);

    }


}

