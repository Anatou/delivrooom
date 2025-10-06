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
        GraphicsContext gc = canvas.getGraphicsContext2D();


        // Dessiner les nœuds
        double width = 800;
        double height = 600;
        double padding = 50; // marge de 50px tout autour

        double minLat = nodes.stream().mapToDouble(XMLReader.Node::getLatitude).min().orElse(0);
        double maxLat = nodes.stream().mapToDouble(XMLReader.Node::getLatitude).max().orElse(0);
        double minLon = nodes.stream().mapToDouble(XMLReader.Node::getLongitude).min().orElse(0);
        double maxLon = nodes.stream().mapToDouble(XMLReader.Node::getLongitude).max().orElse(0);

        // Calcul des rapports sans empiéter sur les bords (merci chatGPT)
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


        Pane root = new Pane(canvas);
        stage.setScene(new Scene(root));
        stage.setTitle("Carte");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
