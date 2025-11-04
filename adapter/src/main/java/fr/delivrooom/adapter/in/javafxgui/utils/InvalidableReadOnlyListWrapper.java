package fr.delivrooom.adapter.in.javafxgui.utils;

import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ObservableList;

/**
 * A simple extension of {@link ReadOnlyListWrapper} that exposes a public
 * {@code invalidate()} method. This is useful for manually triggering change listeners
 * on a read-only list property when the underlying list's contents have changed in a
 * way that doesn't automatically fire an event (e.g., modifying an element's internal state).
 *
 * @param <T> The type of the elements in the list.
 */
public class InvalidableReadOnlyListWrapper<T> extends ReadOnlyListWrapper<T> {

    /**
     * Constructs a new InvalidableReadOnlyListWrapper.
     *
     * @param value The initial list to wrap.
     */
    public InvalidableReadOnlyListWrapper(ObservableList<T> value) {
        super(value);
    }

    /**
     * Manually fires a change event to notify listeners that the list should be considered changed.
     */
    public void invalidate() {
        super.fireValueChangedEvent();
    }
}
