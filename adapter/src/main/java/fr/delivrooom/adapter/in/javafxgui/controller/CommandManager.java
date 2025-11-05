package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.utils.InvalidableReadOnlyObjectWrapper;
import javafx.beans.Observable;

import java.util.Stack;

/**
 * Manages the execution, undo, and redo of commands.
 * It maintains two stacks: one for undo operations and one for redo operations.
 */
public class CommandManager {

    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();
    private final InvalidableReadOnlyObjectWrapper<Object> triggerChanges = new InvalidableReadOnlyObjectWrapper<>(null);

    /**
     * Executes a command, adds it to the undo stack, and clears the redo stack.
     *
     * @param command The command to execute.
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        triggerChanges.invalidate();
    }

    /**
     * Undoes the most recent command.
     * The command is moved from the undo stack to the redo stack.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            triggerChanges.invalidate();
        }
    }

    /**
     * Redoes the most recently undone command.
     * The command is moved from the redo stack back to the undo stack.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            triggerChanges.invalidate();
        }
    }

    /**
     * Retrieves the next command to be undone without removing it from the stack.
     *
     * @return The next command in the undo stack, or null if the stack is empty.
     */
    public Command getNextUndoCommand() {
        if (!undoStack.isEmpty()) {
            return undoStack.peek();
        }
        return null;
    }

    /**
     * Retrieves the next command to be redone without removing it from the stack.
     *
     * @return The next command in the redo stack, or null if the stack is empty.
     */
    public Command getNextRedoCommand() {
        if (!redoStack.isEmpty()) {
            return redoStack.peek();
        }
        return null;
    }

    /**
     * Returns an observable that is invalidated whenever the command history changes.
     * This can be used to update UI elements like undo/redo buttons.
     *
     * @return An Observable that fires change events.
     */
    public Observable getTriggerChanges() {
        return triggerChanges.getReadOnlyProperty();
    }
}
