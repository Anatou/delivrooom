package fr.insalyon.delivrooom;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.*;

public class XMLReader {

    public static List<Node> readNodes() {
        List<Node> nodes = new ArrayList<>();
        try {
            InputStream inputStream = XMLReader.class.getResourceAsStream("/fr/insalyon/delivrooom/xml/petitPlan.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList noeuds = document.getElementsByTagName("noeud");
            for (int i = 0; i < noeuds.getLength(); i++) {
                Element noeud = (Element) noeuds.item(i);
                double latitude = Double.parseDouble(noeud.getAttribute("latitude"));
                double longitude = Double.parseDouble(noeud.getAttribute("longitude"));
                String id = noeud.getAttribute("id");
                nodes.add(new Node(id, latitude, longitude));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodes;
    }

    public static List<Troncon> readTroncons(List<Node> nodes) {
        List<Troncon> routes = new ArrayList<>();
        try {
            InputStream inputStream = XMLReader.class.getResourceAsStream("/fr/insalyon/delivrooom/xml/petitPlan.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList troncons = document.getElementsByTagName("troncon");
            for (int i = 0; i < troncons.getLength(); i++) {
                Element troncon = (Element) troncons.item(i);
                String origine = troncon.getAttribute("origine");
                String destination = troncon.getAttribute("destination");
                Node from = nodes.stream().filter(n -> n.getId().equals(origine)).findFirst().orElse(null);
                Node to = nodes.stream().filter(n -> n.getId().equals(destination)).findFirst().orElse(null);
                String nomRue = troncon.getAttribute("nomRue");
                if (from != null && to != null) {
                    routes.add(new Troncon(from, to, nomRue));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    public static class Node {
        private String id;
        private double x, y;
        public Node(String id, double x, double y) { this.id = id; this.x = x; this.y = y; }
        public String getId() { return id; }
        public double getLatitude() { return x; }
        public double getLongitude() { return y; }
    }

    public static class Troncon {
        private Node from, to;
        private String nomRue;
        public Troncon(Node from, Node to, String nomRue) { this.from = from; this.to = to;  this.nomRue = nomRue; }
        public Node getFrom() { return from; }
        public Node getTo() { return to; }
    }
}
