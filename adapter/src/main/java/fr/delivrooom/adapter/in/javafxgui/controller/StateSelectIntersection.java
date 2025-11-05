package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.*;

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
        return CommandResult.success(new ReverseCommand(new CommandAddDelivery(controller, delivery)));
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
    public void requestCalculateTour(Courier courier) {
        if (courier != null) {
            controller.doCalculateTourForCourier(courier);
        } else {
            controller.doCalculateTour();
        }
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
        return CommandResult.success(new ReverseCommand(new CommandAddCourier(controller, courier)));
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
            controller.doSaveTourSolution(filename);
        }
        else {
            controller.showError("No tour calculated", "No tour to save from any courier");
        }
    }

    public CommandResult createLoadTourCommand(CityMap sourceCityMap, DeliveriesDemand sourceDeliveriesDemand, String filename) {
        return CommandResult.success(new CommandLoadTourSolution(controller, this, sourceCityMap, sourceDeliveriesDemand, filename));
    }
}
