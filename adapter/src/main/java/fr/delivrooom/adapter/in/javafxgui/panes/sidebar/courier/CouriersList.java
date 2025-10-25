package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Courier;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ListView;


public class CouriersList extends ListView<Courier> {

    public static final double COURIER_ITEM_HEIGHT = 35;

    public CouriersList() {
        super();

        prefHeightProperty().bind(Bindings.size(getItems()).multiply(COURIER_ITEM_HEIGHT));
        setBorder(null);

        AppController.getController().couriersProperty().addListener((InvalidationListener) o -> refresh());
        AppController.getController().couriersProperty().addListener((javafx.collections.ListChangeListener.Change<? extends Courier> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    getItems().addAll(c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    getItems().removeAll(c.getRemoved());
                }
            }
        });

        setCellFactory(listView ->
                new CourierListItem(this::deleteCourier, this::calculateCourierRoute, this::toggleCourierVisibility)
        );
    }

    private void deleteCourier(Courier courier) {
        AppController.getController().requestRemoveCourier(courier);
    }

    private void toggleCourierVisibility(Courier courier) {
        courier.setDisplayTourSolution(!courier.isDisplayTourSolution());
        AppController.getController().invalidateCouriers();
    }

    private void calculateCourierRoute(Courier courier) {
        AppController.getController().requestCalculateCourierTour(courier);
    }
}
