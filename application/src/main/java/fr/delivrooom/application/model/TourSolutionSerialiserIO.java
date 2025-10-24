package fr.delivrooom.application.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TourSolutionSerialiserIO {
    public TourSolutionSerialiserIO() { /* utilitaire */ }

    public void saveTourSolutionSerialization(TourSolutionSerialiser serial, String filename) throws IOException {
        // Write
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(serial);
        }
        System.out.println("Successfully dumped Serialisation object");
    }

    public TourSolutionSerialiser loadTourSolutionSerialization(String filename) throws IOException, ClassNotFoundException {
        TourSolutionSerialiser obj;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            obj = (TourSolutionSerialiser) ois.readObject();
        }

        return obj;
    }
}
