package fr.delivrooom.adapter.in.javafxgui.command;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;

public record AddDeliveryCommand(AppController controller, Delivery delivery) implements Command {

    @Override
    public void execute() {

        controller.addDelivery(delivery);

    }

    @Override
    public void undo() {
        controller.removeDelivery(delivery);

    }


}

