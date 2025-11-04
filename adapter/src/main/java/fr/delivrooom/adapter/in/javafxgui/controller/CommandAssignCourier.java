package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
import fr.delivrooom.application.model.TourSolution;

/**
 * Command to assign a courier to a delivery.
 * Supports undo by restoring the previous courier assignment.
 */
public class CommandAssignCourier implements Command {

    private final AppController controller;

    private final Delivery delivery;
    private final Intersection store;

    private final Courier newCourier;
    private TourSolution newCourierPreviousTourSolution = null;

    private final Courier oldCourier;
    private TourSolution oldCourierPreviousTourSolution = null;

    public CommandAssignCourier(AppController controller, Delivery delivery, Intersection store, Courier newCourier) {
        this.controller = controller;
        this.store = store;
        this.delivery = delivery;

        this.oldCourier = controller.getCourierForDelivery(delivery);
        if (oldCourier != null) {
            this.oldCourierPreviousTourSolution = oldCourier.getTourSolution();
        }
        this.newCourier = newCourier;
        if (newCourier != null) {
            this.newCourierPreviousTourSolution = newCourier.getTourSolution();
        }
    }

    @Override
    public void execute() {
        Courier assignedCourier = controller.getCourierForDelivery(delivery);
        if (assignedCourier != null) {
            if (!assignedCourier.equals(newCourier)) {
                assignedCourier.removeDelivery(delivery);
                assignedCourier.deleteTourSolution();
                if (newCourier != null) {
                    newCourier.addDelivery(delivery, store);
                    newCourier.deleteTourSolution();
                }
            }
        } else if (newCourier != null) {
            newCourier.addDelivery(delivery, store);
            newCourier.deleteTourSolution();
        }
        controller.deliveriesDemand.invalidate();
        controller.couriers.invalidate();
    }

    @Override
    public void undo() {
        Courier assignedCourier = controller.getCourierForDelivery(delivery);
        if (assignedCourier != null) {
            if (!assignedCourier.equals(oldCourier)) {
                assignedCourier.removeDelivery(delivery);
                assignedCourier.deleteTourSolution();
                if (assignedCourier.equals(newCourier)) {
                    newCourier.setTourSolution(newCourierPreviousTourSolution);
                }
                if (oldCourier != null) {
                    oldCourier.addDelivery(delivery, store);
                    oldCourier.setTourSolution(oldCourierPreviousTourSolution);
                }
            }
        } else if (oldCourier != null) {
            oldCourier.addDelivery(delivery, store);
            oldCourier.setTourSolution(oldCourierPreviousTourSolution);
        }
        controller.deliveriesDemand.invalidate();
        controller.couriers.invalidate();
    }

    @Override
    public String getStringDescription() {
        if (newCourier == null) {
            return "Unassign Courier from " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
        }
        return "Assign Courier " + newCourier.getId() + " to " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
    }

    @Override
    public String getStringReversedDescription() {
        if (oldCourier == null) {
            return "Unassign Courier from " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
        }
        return "Assign Courier " + oldCourier.getId() + " to " + delivery.takeoutIntersection().getId() + " > " + delivery.deliveryIntersection().getId();
    }
}
