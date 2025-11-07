package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.*;

import java.net.URL;
import java.util.List;

/**
 * Deliveries loaded state - both map and deliveries have been loaded.
 * Allows most operations including adding/removing deliveries and calculating tours.
 */
public record StateDeliveriesLoaded(AppController controller) implements State {

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
        controller.showError("Not in selection mode",
                "Please click 'Select Intersection' button first to enter selection mode.");
    }

    @Override
    public void requestIntersectionSelection() {
        controller.transitionToState(new StateSelectIntersection(controller));
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
        return "DeliveriesLoadedState";
    }

    @Override
    public void saveTour(String filename) {
        // in this state, there might be tours calculated for some couriers
        boolean anyTourToSave = false;
        //System.out.println("Checking for tours to save...");

        List<Courier> courierList = controller.couriersProperty();
        for (Courier courier : courierList) {
            if (courier.getTourSolution() != null) {
                anyTourToSave = true;
                break;
            }
        }
        if (anyTourToSave) {
            //System.out.println("Check tour saved...");
            controller.doSaveTourSolution(filename);
        }
        else {
            controller.showError("No tour calculated", "No tour to save from any courier");
        }
    }

    /**
     * Creates a command to load a tour solution from a file.
     *
     * @param sourceCityMap          The city map before loading, for undo.
     * @param sourceDeliveriesDemand The deliveries demand before loading, for undo.
     * @param sourceCouriers         The list of couriers before loading, for undo.
     * @param filename               The path to the file to load the tour solution from.
     * @return A {@link CommandResult} containing the load command or an error.
     */
    public CommandResult createLoadTourCommand(CityMap sourceCityMap, DeliveriesDemand sourceDeliveriesDemand, String filename) {
        return CommandResult.success(new CommandLoadTourSolution(controller, this, sourceCityMap, sourceDeliveriesDemand, filename));
    }
}
