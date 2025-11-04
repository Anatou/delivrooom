package fr.delivrooom.application.model;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Represents a courier who performs deliveries.
 * Each courier has a unique ID, an assigned set of deliveries, and a calculated tour solution.
 */
public class Courier implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private DeliveriesDemand deliveriesDemand;
    private TourSolution tourSolution;
    private boolean displayTourSolution;

    /**
     * Constructs a new Courier with a given ID.
     *
     * @param id The unique identifier for the courier.
     */
    public Courier(int id) {
        this.id = id;
        this.deliveriesDemand = null;
        this.tourSolution = null;
        this.displayTourSolution = true;
    }

    /**
     * @return The calculated {@link TourSolution} for this courier, or {@code null} if not calculated.
     */
    public TourSolution getTourSolution() {
        return tourSolution;
    }

    /**
     * Sets the calculated tour solution for this courier.
     *
     * @param tourSolution The new tour solution.
     */
    public void setTourSolution(TourSolution tourSolution) {
        this.tourSolution = tourSolution;
    }

    /**
     * @return The unique ID of the courier.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The {@link DeliveriesDemand} assigned to this courier.
     */
    public DeliveriesDemand getDeliveriesDemand() {
        return deliveriesDemand;
    }

    /**
     * Sets the deliveries demand for this courier.
     *
     * @param deliveriesDemand The new deliveries demand.
     */
    public void setDeliveriesDemand(DeliveriesDemand deliveriesDemand) {
        this.deliveriesDemand = deliveriesDemand;
    }

    /**
     * @return {@code true} if the courier has at least one delivery assigned, {@code false} otherwise.
     */
    public boolean hasDeliveriesDemand() {
        return deliveriesDemand != null && !deliveriesDemand.deliveries().isEmpty();
    }

    /**
     * Deletes the current tour solution, typically when the assigned deliveries change.
     */
    public void deleteTourSolution() {
        this.tourSolution = null;
    }

    /**
     * Adds a delivery to this courier's demand.
     * If the courier has no deliveries, a new {@link DeliveriesDemand} is created.
     *
     * @param delivery The delivery to add.
     * @param store    The warehouse/store associated with the deliveries.
     */
    public void addDelivery(Delivery delivery, Intersection store) {
        if (this.deliveriesDemand == null || this.deliveriesDemand.deliveries().isEmpty()) {
            this.deliveriesDemand = new DeliveriesDemand(new ArrayList<>(), store);
        }
        this.deliveriesDemand.deliveries().add(delivery);
    }

    /**
     * Removes a delivery from this courier's demand.
     *
     * @param delivery The delivery to remove.
     */
    public void removeDelivery(Delivery delivery) {
        if (this.deliveriesDemand != null) {
            this.deliveriesDemand.deliveries().remove(delivery);
        }
    }

    /**
     * @return {@code true} if the courier's tour should be displayed on the map, {@code false} otherwise.
     */
    public boolean isDisplayTourSolution() {
        return displayTourSolution;
    }

    /**
     * Sets the visibility of the courier's tour on the map.
     *
     * @param displayTourSolution {@code true} to display the tour, {@code false} to hide it.
     */
    public void setDisplayTourSolution(boolean displayTourSolution) {
        this.displayTourSolution = displayTourSolution;
    }

    /**
     * @return A string representation of the courier, e.g., "Courier 1".
     */
    @Override
    public String toString() {
        return "Courier " + id;
    }
}
