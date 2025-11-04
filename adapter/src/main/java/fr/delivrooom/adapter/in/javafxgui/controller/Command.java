package fr.delivrooom.adapter.in.javafxgui.controller;


/**
 * Represents an operation that can be executed and undone.
 * This is the core of the Command design pattern implementation.
 */
public interface Command {

    /**
     * Executes the command's operation.
     */
    void execute();

    /**
     * Reverts the changes made by the execute() method.
     */
    void undo();

    /**
     * Gets a user-friendly description of the command's action.
     *
     * @return A string describing what the command does.
     */
    String getStringDescription();

    /**
     * Gets a user-friendly description of the command's undo action.
     *
     * @return A string describing what undoing the command does.
     */
    String getStringReversedDescription();
}
