package fr.delivrooom.application.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TourSolutionSerialiserIO {
    public TourSolutionSerialiserIO() { /* utilitaire */ }

    public void saveList(List<TourSolution> list, String filename) throws IOException {
        // Write
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TourSolution.bin"))) {
            oos.writeObject(list);
        }
        System.out.println("Successfully dumped TourSolution object");
    }

    @SuppressWarnings("unchecked")
    public List<TourSolution> loadList(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<TourSolution>) obj;
            } else {
                return new ArrayList<>();
            }
        }
    }
}
