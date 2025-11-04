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

    public CommandAddDelivery(AppController controller, Delivery delivery) {
        this.delivery = delivery;
        this.controller = controller;

        // In case the delivery already exists (in case of ReverseCommand(this), we store the delivery index and associated courier)
        deliveryIndex = controller.deliveriesDemand.get().deliveries().indexOf(delivery);
        associatedCourier = controller.getCourierForDelivery(delivery);
        if (associatedCourier != null) {
            associatedTourSolution = associatedCourier.getTourSolution();
        }
    }

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

    @Override
    public void undo() {
        controller.deliveriesDemand.get().deliveries().remove(delivery);
        // Also remove from any courier assigned to it (and invalidate their tour)
        if (associatedCourier != null) {
            associatedCourier.removeDelivery(delivery);
            associatedCourier.deleteTourSolution();
            controller.couriers.invalidate();
        }
        controller.deliveriesDemand.invalidate();
    }

    @Override
    public String toString() {
        return "Add Delivery " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
    }
}

