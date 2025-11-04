package fr.delivrooom.adapter.in.javafxgui.controller;

import fr.delivrooom.adapter.in.javafxgui.utils.InvalidableReadOnlyObjectWrapper;
import javafx.beans.Observable;

import java.util.Stack;

public class CommandManager {

    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();
    private final InvalidableReadOnlyObjectWrapper<Object> triggerChanges = new InvalidableReadOnlyObjectWrapper<>(null);


    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        triggerChanges.invalidate();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            triggerChanges.invalidate();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            triggerChanges.invalidate();
        }
    }

    public Command getNextUndoCommand() {
        if (!undoStack.isEmpty()) {
            return undoStack.peek();
        }
        return null;
    }

    public Command getNextRedoCommand() {
        if (!redoStack.isEmpty()) {
            return redoStack.peek();
        }
        return null;
    }

    public Observable getTriggerChanges() {
        return triggerChanges;
    }
}
