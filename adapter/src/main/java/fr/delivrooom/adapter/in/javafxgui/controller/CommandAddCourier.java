package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;

/**
 * Command to add a courier to the system.
 * Supports undo by removing the courier.
 */
public class CommandAddCourier implements Command {

    private final AppController controller;
    private final Courier courier;
    private int courierIndex;

    public CommandAddCourier(AppController controller, Courier courier) {
        this.controller = controller;
        this.courier = courier;

        // In case of a ReverseCommand(this), we store the courier index
        courierIndex = controller.couriers.indexOf(courier);
    }

    @Override
    public void execute() {
        if (courierIndex != -1) {
            controller.couriers.add(courierIndex, courier);
        } else {
            controller.couriers.add(courier);
        }
    }

    @Override
    public void undo() {
        courierIndex = controller.couriers.indexOf(courier);
        controller.couriers.remove(courier);
    }

    @Override
    public String getStringDescription() {
        return "Add Courier " + courier.getId();
    }

    @Override
    public String getStringReversedDescription() {
        return "Remove Courier " + courier.getId();
    }
}
