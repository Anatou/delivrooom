package fr.delivrooom.adapter.in.javafxgui.command;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;

public record RemoveDeliveryCommand(AppController controller, Delivery delivery) implements Command {

    @Override
    public void execute() {
        controller.removeDelivery(delivery);
    }

    @Override
    public void undo() {
        controller.addDelivery(delivery);

    }


}

