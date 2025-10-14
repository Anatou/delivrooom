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
import java.util.HashMap;

public class XMLCityMapLoader implements CityMapRepository {

    @Override
    public CityMap getCityMap(URL cityMapURL) throws Exception {
        HashMap<Long, Intersection> intersections = new HashMap<>();
        HashMap<Long, HashMap<Long, Road>> roads = new HashMap<>();

        InputStream inputStream = cityMapURL.openStream();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();

        NodeList intersectionsNodes = document.getElementsByTagName("noeud");
        if (intersectionsNodes.getLength() == 0) {
            throw new Exception("No intersections found in the XML file.");
        }
        for (int i = 0; i < intersectionsNodes.getLength(); i++) {
            Element node = (Element) intersectionsNodes.item(i);
            double latitude = Double.parseDouble(node.getAttribute("latitude"));
            double longitude = Double.parseDouble(node.getAttribute("longitude"));
            long id = Long.parseLong(node.getAttribute("id"));
            intersections.putIfAbsent(id, new Intersection(id, latitude, longitude));
        }

        NodeList roadNodes = document.getElementsByTagName("troncon");
        if (roadNodes.getLength() == 0) {
            throw new Exception("No roads found in the XML file.");
        }
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

        return new CityMap(intersections, roads);
    }
}
