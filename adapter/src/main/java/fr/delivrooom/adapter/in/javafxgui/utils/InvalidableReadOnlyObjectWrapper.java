package fr.delivrooom.adapter.in.javafxgui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;

public class InvalidableReadOnlyObjectWrapper<T> extends ReadOnlyObjectWrapper<T> {

    public InvalidableReadOnlyObjectWrapper(T value) {
        super(value);
    }

    public void invalidate() {
        super.fireValueChangedEvent();
    }
}
