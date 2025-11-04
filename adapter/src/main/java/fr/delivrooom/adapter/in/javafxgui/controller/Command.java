package fr.delivrooom.adapter.in.javafxgui.controller;


public interface Command {

    void execute();

    void undo();

    String getStringDescription();

    String getStringReversedDescription();
}
