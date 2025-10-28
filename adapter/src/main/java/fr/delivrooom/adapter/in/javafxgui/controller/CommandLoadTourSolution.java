package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandLoadTourSolution implements Command{

    private final AppController controller;
    private final State sourceState;
    private final CityMap sourceCityMap;
    private final DeliveriesDemand sourceDeliveriesDemand;
    private final List<Courier> sourceCouriers;
    private final String filename;

    public CommandLoadTourSolution(AppController controller, State sourceState, CityMap sourceCityMap, DeliveriesDemand sourceDeliveriesDemand, List<Courier> sourceCouriers, String filename) {
        this.controller = controller;
        this.sourceState = sourceState;
        this.sourceCityMap = sourceCityMap;
        this.sourceDeliveriesDemand = sourceDeliveriesDemand;
        this.sourceCouriers = sourceCouriers;
        this.filename = filename;
    }

    @Override
    public void execute() {
        TourSolutionSerialiserIO tourSolutionSerialiserIO = new TourSolutionSerialiserIO();
        TourSolutionSerialiser tourSolutionSerialiser;
        try {
            tourSolutionSerialiser = tourSolutionSerialiserIO.loadTourSolutionSerialization(filename);
        } catch (IOException | ClassNotFoundException e) {
            controller.showError("Tour loading failure", "Failed to load tour");
            return;
        }

        controller.doRestoreCityMap(tourSolutionSerialiser.cityMap());
        controller.doRestoreDeliveriesDemand(tourSolutionSerialiser.demand());

        // create as many couriers as necessary
        int couriersNumber = tourSolutionSerialiser.tourSolutionList().size();
        List<Courier> newCouriers = new ArrayList<>();
        for (int i = 0; i < couriersNumber; ++i){
            newCouriers.add(new Courier(i));
        }
        controller.couriersProperty().setAll(newCouriers);

        for (int i = 0; i < couriersNumber; ++i){
            controller.couriersProperty().get(i).setTourSolution(tourSolutionSerialiser.tourSolutionList().get(i));
        }
    }

    @Override
    public void undo() {

        controller.doRestoreCityMap(sourceCityMap);
        controller.doRestoreDeliveriesDemand(sourceDeliveriesDemand);
        controller.couriersProperty().setAll(sourceCouriers);
        controller.transitionToState(sourceState);
    }

    public void redo() {
        execute();
    }
}
