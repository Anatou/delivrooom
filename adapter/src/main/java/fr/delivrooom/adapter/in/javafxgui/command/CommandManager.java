package fr.delivrooom.adapter.in.javafxgui.command;

import java.util.Stack;

public class CommandManager {

    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        if(command.execute()){
            undoStack.push(command);
            redoStack.clear();
        }
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            if(command.undo()) {
                //System.out.println("command.undo");
                redoStack.push(command);
            }else{
                //System.out.println("no undo");
                undoStack.push(command);
            }
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            if(command.execute()) {
                //System.out.println("command.redo");
                undoStack.push(command);
            }else{
                //System.out.println("no redo");
                redoStack.push(command);
            }
        }
    }
}
