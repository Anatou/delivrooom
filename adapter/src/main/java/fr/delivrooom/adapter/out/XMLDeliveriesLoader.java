package fr.delivrooom.adapter.out;

import fr.delivrooom.application.model.CityMap;
import fr.delivrooom.application.model.DeliveriesDemand;
import fr.delivrooom.application.model.Delivery;
import fr.delivrooom.application.model.Intersection;
import fr.delivrooom.application.port.out.DeliveriesRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XMLDeliveriesLoader implements DeliveriesRepository {

    @Override
    public DeliveriesDemand getDeliveriesDemand(CityMap cityMap, URL deliveriesURL) {
        List<Delivery> deliveries = new ArrayList<>();
        Intersection store = null;
        try {
            InputStream inputStream = deliveriesURL.openStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            Element storeElement = (Element) document.getElementsByTagName("entrepot").item(0);
            long storeId = Long.parseLong(storeElement.getAttribute("adresse"));

            // Use coordinates from the city map
            store = cityMap.getIntersections().values().stream()
                    .filter(intersection -> intersection.getId() == storeId)
                    .findFirst()
                    .orElseThrow(() -> new Exception("Store intersection not found in city map"));

            NodeList deliveryNodes = document.getElementsByTagName("livraison");
            for (int i = 0; i < deliveryNodes.getLength(); i++) {
                Element node = (Element) deliveryNodes.item(i);
                long takeoutId = Long.parseLong(node.getAttribute("adresseEnlevement"));
                long deliveryId = Long.parseLong(node.getAttribute("adresseLivraison"));
                int takeoutDuration = Integer.parseInt(node.getAttribute("dureeEnlevement"));
                int deliveryDuration = Integer.parseInt(node.getAttribute("dureeLivraison"));

                // Use coordinates from the city map
                Intersection takeoutIntersection = cityMap.getIntersections().values().stream()
                        .filter(intersection -> intersection.getId() == takeoutId)
                        .findFirst()
                        .orElseThrow(() -> new Exception("Takeout intersection not found in city map"));

                Intersection deliveryIntersection = cityMap.getIntersections().values().stream()
                        .filter(intersection -> intersection.getId() == deliveryId)
                        .findFirst()
                        .orElseThrow(() -> new Exception("Delivery intersection not found in city map"));
                deliveries.add(new Delivery(takeoutIntersection, deliveryIntersection, takeoutDuration, deliveryDuration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DeliveriesDemand(deliveries, store);
    }
}
