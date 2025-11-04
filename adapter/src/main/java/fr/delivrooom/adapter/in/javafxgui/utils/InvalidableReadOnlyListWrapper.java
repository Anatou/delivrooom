package fr.delivrooom.adapter.in.javafxgui.utils;

import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ObservableList;

public class InvalidableReadOnlyListWrapper<T> extends ReadOnlyListWrapper<T> {

    public InvalidableReadOnlyListWrapper(ObservableList<T> value) {
        super(value);
    }

    public void invalidate() {
        super.fireValueChangedEvent();
    }
}
