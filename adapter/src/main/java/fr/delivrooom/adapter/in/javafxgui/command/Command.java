package fr.delivrooom.adapter.in.javafxgui.command;


public interface Command {
    boolean execute();

    boolean undo();
}
