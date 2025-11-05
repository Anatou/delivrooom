package fr.delivrooom.adapter.in.javafxgui.controller;

/**
 * A decorator that reverses the execution and undo logic of a given command.
 * When {@code execute()} is called on a {@code ReverseCommand}, it calls {@code undo()} on the wrapped command.
 * When {@code undo()} is called, it calls {@code execute()} on the wrapped command.
 * <p>
 * This is useful for creating opposite commands without writing a new class. For example,
 * a {@code CommandRemoveDelivery} can be implemented as {@code new ReverseCommand(new CommandAddDelivery(...))}.
 *
 * @param command The command to be reversed.
 */
public record ReverseCommand(Command command) implements Command {

    /**
     * Executes the reverse action by calling the wrapped command's {@code undo()} method.
     */
    @Override
    public void execute() {
        command.undo();
    }

    /**
     * Undoes the reverse action by calling the wrapped command's {@code execute()} method.
     */
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
