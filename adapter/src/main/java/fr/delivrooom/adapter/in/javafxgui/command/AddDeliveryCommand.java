package fr.delivrooom.adapter.in.javafxgui.command;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;

public record AddDeliveryCommand(AppController controller, Delivery delivery) implements Command {

    @Override
    public boolean execute() {
        return controller.addDelivery(delivery);
    }

    @Override
    public boolean undo() {
        return controller.removeDelivery(delivery);

    }


}

