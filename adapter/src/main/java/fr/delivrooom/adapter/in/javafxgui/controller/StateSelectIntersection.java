package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;

import java.net.URL;
import java.util.List;

/**
 * Select intersection state - user is in intersection selection mode.
 * Allows selecting an intersection and returns to DeliveriesLoaded state after selection.
 */
public record StateSelectIntersection(AppController controller) implements State {

    @Override
    public CommandResult createOpenMapCommand(URL url) {
        return CommandResult.success(new CommandLoadMap(controller, url, this));
    }

    @Override
    public CommandResult createOpenDeliveriesCommand(URL url) {
        return CommandResult.success(new CommandLoadDeliveries(controller, url, this));
    }

    @Override
    public CommandResult createAddDeliveryCommand(Delivery delivery) {
        return CommandResult.success(new CommandAddDelivery(controller, delivery));
    }

    @Override
    public CommandResult createRemoveDeliveryCommand(Delivery delivery) {
        return CommandResult.success(new CommandRemoveDelivery(controller, delivery));
    }

    @Override
    public void selectIntersection(Intersection intersection) {
        controller.doSelectIntersection(intersection);
        controller.transitionToState(new StateDeliveriesLoaded(controller));
    }

    @Override
    public void requestIntersectionSelection() {
        // no-op
    }

    @Override
    public CommandResult createCalculateTourCommand() {
        return CommandResult.success(new CommandCalculateTour(controller));
    }

    @Override
    public CommandResult createCalculateCourierTourCommand(Courier courier) {
        return CommandResult.success(new CommandCalculateCourierTour(controller, courier));
    }

    @Override
    public CommandResult createAssignCourierCommand(Delivery delivery, Courier courier) {
        Intersection store = controller.deliveriesDemandProperty().getValue().store();
        return CommandResult.success(new CommandAssignCourier(controller, delivery, store, courier));
    }

    @Override
    public CommandResult createAddCourierCommand(Courier courier) {
        return CommandResult.success(new CommandAddCourier(controller, courier));
    }

    @Override
    public CommandResult createRemoveCourierCommand(Courier courier) {
        return CommandResult.success(new CommandRemoveCourier(controller, courier));
    }

    @Override
    public String getStateName() {
        return "SelectIntersectionState";
    }

    @Override
    public void saveTour(String filename) {
        // in this state, there might be tours calculated for some couriers
        boolean anyTourToSave = false;
        System.out.println("Checking for tours to save...");

        List<Courier> courierList = controller.couriersProperty();
        for (Courier courier : courierList) {
            if (courier.getTourSolution() != null) {
                anyTourToSave = true;
                break;
            }
        }
        if (anyTourToSave) {
            System.out.println("Check tour saved...");
            controller.saveTourSolution(filename);
        }
        else {
            controller.showError("No tour calculated", "No tour to save from any courier");
        }
    }
}
