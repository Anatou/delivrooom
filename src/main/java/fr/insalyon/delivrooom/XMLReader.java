package fr.insalyon.delivrooom;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XMLReader {
    public static void main(String[] args) {
        try {

            String XMLName = "petitPlan.xml";
            InputStream inputStream = XMLReader.class.getResourceAsStream("/fr/insalyon/delivrooom/xml/" + XMLName);
            if (inputStream == null) {
                throw new FileNotFoundException("Fichier map.xml non trouvé dans les ressources.");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            document.getDocumentElement().normalize();

            NodeList noeuds = document.getElementsByTagName("noeud");

            for (int i = 0; i < noeuds.getLength(); i++) {
                Element noeud = (Element) noeuds.item(i);

                String id = noeud.getAttribute("id");
                double latitude = Double.parseDouble(noeud.getAttribute("latitude"));
                double longitude = Double.parseDouble(noeud.getAttribute("longitude"));

                System.out.println("Noeud " + id + " → (" + latitude + ", " + longitude + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
