package fr.delivrooom.adapter.in.javafxgui.controller;

/**
 * Command to request entering intersection selection mode.
 * Supports undo by returning to the previous state.
 */
public class CommandRequestIntersectionSelection implements Command {

    private final AppController controller;
    private final State sourceState;

    public CommandRequestIntersectionSelection(AppController controller, State sourceState) {
        this.controller = controller;
        this.sourceState = sourceState;
    }

    @Override
    public void execute() {
        controller.transitionToState(new StateSelectIntersection(controller));
    }

    @Override
    public void undo() {
        controller.transitionToState(sourceState);
    }
}
