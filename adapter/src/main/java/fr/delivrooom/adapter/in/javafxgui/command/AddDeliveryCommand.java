package fr.delivrooom.adapter.in.javafxgui.command;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Delivery;

public class AddDeliveryCommand implements Command{

        private final AppController controller;
        private final Delivery delivery;

        public AddDeliveryCommand(AppController controller, Delivery delivery) {
            this.controller = controller;
            this.delivery = delivery;
        }

        @Override
        public void execute() {
            controller.addDelivery(delivery);
        }

        @Override
        public void undo() {
            controller.removeDelivery(delivery);

        }


}

