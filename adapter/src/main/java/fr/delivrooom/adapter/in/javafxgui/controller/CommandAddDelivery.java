package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.TourSolution;

/**
 * Command to add a delivery to the deliveries demand.
 * Supports undo by removing the delivery.
 */
public class CommandAddDelivery implements Command {

    private final AppController controller;
    private final Delivery delivery;
    private Courier associatedCourier;
    private TourSolution associatedTourSolution = null;
    private int deliveryIndex;

    /**
     * Creates a command to add a delivery.
     *
     * @param controller The main application controller.
     * @param delivery   The delivery to be added.
     */
    public CommandAddDelivery(AppController controller, Delivery delivery) {
        this.delivery = delivery;
        this.controller = controller;

        // In case the delivery already exists (in case of ReverseCommand(this), we store the delivery index and associated courier)
        if (controller.deliveriesDemandProperty().getValue() != null) {
            deliveryIndex = controller.deliveriesDemandProperty().getValue().deliveries().indexOf(delivery);
            associatedCourier = controller.getCourierForDelivery(delivery);
            if (associatedCourier != null) {
                associatedTourSolution = associatedCourier.getTourSolution();
            }
        }
    }

    /**
     * Executes the command, adding the delivery to the deliveries demand.
     * If the delivery was previously associated with a courier, this association is restored.
     */
    @Override
    public void execute() {
        if (deliveryIndex == -1) {
            controller.deliveriesDemand.get().deliveries().add(delivery);
            deliveryIndex = controller.deliveriesDemand.get().deliveries().indexOf(delivery);
        } else {
            controller.deliveriesDemand.get().deliveries().add(deliveryIndex, delivery);
        }
        if (associatedCourier != null) {
            associatedCourier.addDelivery(delivery, controller.deliveriesDemand.get().store());
            associatedCourier.setTourSolution(associatedTourSolution);
            controller.couriers.invalidate();
        }
        controller.deliveriesDemand.invalidate();
    }

    /**
     * Undoes the command, removing the delivery from the deliveries demand and from any assigned courier.
     */
    @Override
    public void undo() {
        controller.deliveriesDemand.get().deliveries().remove(delivery);
        // Also remove from any courier assigned to it (and invalidate their tour)
        if (associatedCourier != null) {
            associatedCourier.removeDelivery(delivery);
            associatedCourier.deleteTourSolution();
            controller.invalidateCouriers();
        }
        controller.deliveriesDemand.get().deliveries().remove(delivery);
    }

    @Override
    public String getStringDescription() {
        return "Add Delivery " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
    }

    @Override
    public String getStringReversedDescription() {
        return "Remove Delivery " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
    }
}

