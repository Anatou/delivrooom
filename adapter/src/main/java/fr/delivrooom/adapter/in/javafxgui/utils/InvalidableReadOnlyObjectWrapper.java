package fr.delivrooom.adapter.in.javafxgui.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * A simple extension of {@link ReadOnlyObjectWrapper} that exposes a public
 * {@code invalidate()} method. This is useful for manually triggering change listeners
 * on a read-only object property when its internal state has changed.
 *
 * @param <T> The type of the wrapped object.
 */
public class InvalidableReadOnlyObjectWrapper<T> extends ReadOnlyObjectWrapper<T> {

    /**
     * Constructs a new InvalidableReadOnlyObjectWrapper.
     *
     * @param value The initial object to wrap.
     */
    public InvalidableReadOnlyObjectWrapper(T value) {
        super(value);
    }

    /**
     * Manually fires a change event to notify listeners that the object should be considered changed.
     */
    public void invalidate() {
        super.fireValueChangedEvent();
    }
}
