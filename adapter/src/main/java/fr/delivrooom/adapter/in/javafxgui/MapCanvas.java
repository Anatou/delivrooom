package fr.delivrooom.adapter.in.javafxgui;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.Intersection;
import fr.delivrooom.application.model.Road;
import javafx.beans.InvalidationListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapCanvas extends Canvas {

    public MapCanvas() {
        setupCanvas();
    }

    public MapCanvas(double v, double v1) {
        super(v, v1);
        setupCanvas();
    }

    private void setupCanvas() {
        InvalidationListener canvasResizeListener = (o) -> {
            drawMap(getGraphicsContext2D(), getWidth(), getHeight(), JavaFXApp.guiUseCase().getCityMap());
        };
        widthProperty().addListener(canvasResizeListener);
        heightProperty().addListener(canvasResizeListener);
        canvasResizeListener.invalidated(null);
    }

    private void drawMap(GraphicsContext gc, double width, double height, CityMap cityMap) {
        double padding = 12;
        double minLat = cityMap.getIntersections().stream().mapToDouble(Intersection::getLatitude).min().orElse(0);
        double maxLat = cityMap.getIntersections().stream().mapToDouble(Intersection::getLatitude).max().orElse(0);
        double minLon = cityMap.getIntersections().stream().mapToDouble(Intersection::getLongitude).min().orElse(0);
        double maxLon = cityMap.getIntersections().stream().mapToDouble(Intersection::getLongitude).max().orElse(0);

        gc.clearRect(0, 0, width, height);

        gc.setFill(Color.BLACK);
        for (Intersection node : cityMap.getIntersections()) {
            double x = padding + (node.getLongitude() - minLon) / (maxLon - minLon) * (width - 2 * padding);
            double y = padding + (maxLat - node.getLatitude()) / (maxLat - minLat) * (height - 2 * padding);
            gc.fillOval(x - 3, y - 3, 6, 6);
        }

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        for (Road troncon : cityMap.getRoads()) {
            Intersection origin = troncon.getOrigin();
            Intersection destination = troncon.getDestination();
            double x1 = padding + (origin.getLongitude() - minLon) / (maxLon - minLon) * (width - 2 * padding);
            double y1 = padding + (maxLat - origin.getLatitude()) / (maxLat - minLat) * (height - 2 * padding);
            double x2 = padding + (destination.getLongitude() - minLon) / (maxLon - minLon) * (width - 2 * padding);
            double y2 = padding + (maxLat - destination.getLatitude()) / (maxLat - minLat) * (height - 2 * padding);
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

}
