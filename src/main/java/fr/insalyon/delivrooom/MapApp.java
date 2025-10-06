package fr.insalyon.delivrooom;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class MapApp extends Application {

    @Override
    public void start(Stage stage) {
        List<XMLReader.Node> nodes = XMLReader.readNodes();
        List<XMLReader.Troncon> troncons = XMLReader.readTroncons(nodes);

        Canvas canvas = new Canvas(800, 600);
        Pane root = new Pane(canvas);
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        // Listeners pour redessiner la carte lors du redimensionnement
        canvas.widthProperty().addListener((obs, oldVal, newVal) ->
                drawMap(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight(), nodes, troncons)
        );
        canvas.heightProperty().addListener((obs, oldVal, newVal) ->
                drawMap(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight(), nodes, troncons)
        );

        drawMap(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight(), nodes, troncons);

        stage.setScene(new Scene(root));
        stage.setTitle("Carte");
        stage.show();
    }

    private void drawMap(GraphicsContext gc, double width, double height, List<XMLReader.Node> nodes, List<XMLReader.Troncon> troncons) {
        double padding = 50;
        double minLat = nodes.stream().mapToDouble(XMLReader.Node::getLatitude).min().orElse(0);
        double maxLat = nodes.stream().mapToDouble(XMLReader.Node::getLatitude).max().orElse(0);
        double minLon = nodes.stream().mapToDouble(XMLReader.Node::getLongitude).min().orElse(0);
        double maxLon = nodes.stream().mapToDouble(XMLReader.Node::getLongitude).max().orElse(0);

        gc.clearRect(0, 0, width, height);

        gc.setFill(Color.BLACK);
        for (XMLReader.Node node : nodes) {
            double x = padding + (node.getLongitude() - minLon) / (maxLon - minLon) * (width - 2 * padding);
            double y = padding + (maxLat - node.getLatitude()) / (maxLat - minLat) * (height - 2 * padding);
            gc.fillOval(x - 3, y - 3, 6, 6);
        }

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        for (XMLReader.Troncon troncon : troncons) {
            XMLReader.Node from = troncon.getFrom();
            XMLReader.Node to = troncon.getTo();
            double x1 = padding + (from.getLongitude() - minLon) / (maxLon - minLon) * (width - 2 * padding);
            double y1 = padding + (maxLat - from.getLatitude()) / (maxLat - minLat) * (height - 2 * padding);
            double x2 = padding + (to.getLongitude() - minLon) / (maxLon - minLon) * (width - 2 * padding);
            double y2 = padding + (maxLat - to.getLatitude()) / (maxLat - minLat) * (height - 2 * padding);
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
