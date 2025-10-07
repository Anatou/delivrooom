package fr.delivrooom.adapter.out;

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
import java.util.ArrayList;
import java.util.List;

public class XMLDeliveriesLoader implements DeliveriesRepository {

    @Override
    public DeliveriesDemand getDeliveriesDemand() {
        List<Delivery> deliveries = new ArrayList<>();
        Intersection store = null;
        try {
            InputStream inputStream = getClass().getResourceAsStream("/xml/myDeliverRequest.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();

            Element storeElement = (Element) document.getElementsByTagName("entrepot").item(0);
            long storeId = Long.parseLong(storeElement.getAttribute("adresse"));

            //TODO : remplacer par les vraies coordonnées du noeud
            store = new Intersection(storeId, 0, 0);
            System.out.println("store point : " + storeId);

            NodeList deliveryNodes = document.getElementsByTagName("livraison");
            for (int i = 0; i < deliveryNodes.getLength(); i++) {
                Element node = (Element) deliveryNodes.item(i);
                long takeoutId = Long.parseLong(node.getAttribute("adresseEnlevement"));
                long deliveryId = Long.parseLong(node.getAttribute("adresseLivraison"));
                int takeoutDuration = Integer.parseInt(node.getAttribute("dureeEnlevement"));
                int deliveryDuration = Integer.parseInt(node.getAttribute("dureeLivraison"));
                Intersection takeout = new Intersection(takeoutId, 0, 0);
                Intersection delivery = new Intersection(deliveryId, 0, 0);
                deliveries.add(new Delivery(takeout, delivery, takeoutDuration, deliveryDuration));
                System.out.println("new delivery point : " + takeoutId + " -> " + deliveryId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DeliveriesDemand(deliveries, store);
    }
}
