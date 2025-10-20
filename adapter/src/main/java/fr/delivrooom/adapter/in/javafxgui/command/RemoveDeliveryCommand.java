package fr.delivrooom.adapter.in.javafxgui.command;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;

public record RemoveDeliveryCommand(AppController controller, Delivery delivery) implements Command {

    @Override
    public boolean execute() {
        return controller.removeDelivery(delivery);
    }

    @Override
    public boolean undo() {
        return controller.addDelivery(delivery);

    }


}

