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

    /**
     * Creates a command to add a courier.
     *
     * @param controller The main application controller.
     * @param courier    The courier to be added.
     */
    public CommandAddCourier(AppController controller, Courier courier) {
        this.controller = controller;
        this.courier = courier;

        // In case of a ReverseCommand(this), we store the courier index
        courierIndex = controller.couriersProperty().indexOf(courier);
    }

    /**
     * Executes the command, adding the courier to the system.
     * If the courier was previously removed (and this is a redo), it is re-inserted at its original index.
     */
    @Override
    public void execute() {
        if (courierIndex != -1) {
            controller.couriersProperty().add(courierIndex, courier);
        } else {
            controller.couriersProperty().add(courier);
        }
    }

    /**
     * Undoes the command, removing the courier from the system.
     * The courier's index is stored before removal to allow for proper re-insertion on redo.
     */
    @Override
    public void undo() {
        courierIndex = controller.couriersProperty().indexOf(courier);
        controller.couriersProperty().remove(courier);
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
