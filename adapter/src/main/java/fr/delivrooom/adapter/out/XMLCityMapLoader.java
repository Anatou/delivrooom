package fr.delivrooom.adapter.out;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.Intersection;
import fr.delivrooom.application.model.Road;
import fr.delivrooom.application.port.out.CityMapRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XMLCityMapLoader implements CityMapRepository {

    @Override
    public CityMap getCityMap(URL cityMapURL) {
        HashMap<Long, Intersection> intersections = new HashMap<>();
        HashMap<Long, HashMap<Long, Road>> roads = new HashMap<>();
        try {
            InputStream inputStream = cityMapURL.openStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList intersectionsNodes = document.getElementsByTagName("noeud");
            for (int i = 0; i < intersectionsNodes.getLength(); i++) {
                Element node = (Element) intersectionsNodes.item(i);
                double latitude = Double.parseDouble(node.getAttribute("latitude"));
                double longitude = Double.parseDouble(node.getAttribute("longitude"));
                long id = Long.parseLong(node.getAttribute("id"));
                intersections.putIfAbsent(id, new Intersection(id, latitude, longitude));
            }

            NodeList roadNodes = document.getElementsByTagName("troncon");
            for (int i = 0; i < roadNodes.getLength(); i++) {
                Element node = (Element) roadNodes.item(i);
                Intersection from = intersections.get(Long.parseLong(node.getAttribute("origine")));
                Intersection to = intersections.get(Long.parseLong(node.getAttribute("destination")));
                float length = Float.parseFloat(node.getAttribute("longueur"));
                String name = node.getAttribute("nomRue");

                if (from == null || to == null) {
                    throw new Exception("Invalid road: " + node);
                }
                Road road = new Road(from, to, length, name);
                roads.putIfAbsent(from.getId(), new HashMap<>());
                roads.get(from.getId()).put(to.getId(), road);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CityMap(intersections, roads);
    }
}
