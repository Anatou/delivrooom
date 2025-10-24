package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.application.model.Courier;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.TourSolution;

import java.net.URL;
import java.util.Map;

public class CommandSaveTourSolution implements Command{

    private final AppController controller;
    private final State sourceState;
    private final TourSolution tourSolution;

    public CommandSaveTourSolution(AppController controller, State sourceState, TourSolution tourSolution) {
        this.controller = controller;
        this.sourceState = sourceState;
        this.tourSolution = tourSolution;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {
        // do not allow user to undo
    }
}
