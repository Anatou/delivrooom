package fr.delivrooom.adapter.in.javafxgui.panes.sidebar.courier;

import fr.delivrooom.adapter.in.javafxgui.controller.AppController;
import fr.delivrooom.application.model.Courier;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ListView;

import java.util.List;


public class CouriersList extends ListView<Courier> {

    public static final double COURIER_ITEM_HEIGHT = 35;

    public CouriersList() {
        super();

        prefHeightProperty().bind(Bindings.size(getItems()).multiply(COURIER_ITEM_HEIGHT));
        setBorder(null);

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
                new CourierListItem(this::deleteCourier, this::calculateCourierRoute)
        );
    }

    private void deleteCourier(Courier courier) {
        AppController.getController().requestRemoveCourier(courier);
    }

    private void calculateCourierRoute(Courier courier) {
        AppController appController = AppController.getController();
        List<Courier> couriers = appController.couriersProperty();
        Courier courier_found = null;
        System.out.println("Searching for courier with ID: " + courier.getId());
        for (Courier courier_ : couriers) {
            System.out.println("Checking courier with ID: " + courier_.getId());
            if (courier_.getId() == courier.getId()) {
                courier_found = courier_;
                break;
            }
        }

        // affichage des deliveries demand du courrier
        System.out.println("deliveries demand of :" + courier_found + " \n" + courier_found.getDeliveriesDemand());
        System.out.println("Calculating route for courier " + courier_found);
        appController.requestCalculateCourierTour(courier_found);
    }
}
