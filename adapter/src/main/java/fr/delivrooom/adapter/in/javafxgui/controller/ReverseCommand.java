package fr.delivrooom.adapter.in.javafxgui.controller;

public record ReverseCommand(Command command) implements Command {

    @Override
    public void execute() {
        command.undo();
    }

    @Override
    public void undo() {
        command.execute();
    }

    @Override
    public String getStringDescription() {
        return command.getStringReversedDescription();
    }

    @Override
    public String getStringReversedDescription() {
        return command.getStringDescription();
    }

}
