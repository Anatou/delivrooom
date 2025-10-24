package fr.delivrooom.application.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class TourSolutionSerialiserIO {
    private TourSolutionSerialiserIO() { /* utilitaire */ }

    public static void saveObject(TourSolutionSerialiser obj, File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(obj);
        }
    }

    public static TourSolutionSerialiser loadObject(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (TourSolutionSerialiser) ois.readObject();
        }
    }

    public static void saveList(List<TourSolutionSerialiser> list, File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<TourSolutionSerialiser> loadList(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<TourSolutionSerialiser>) obj;
            } else {
                return new ArrayList<>();
            }
        }
    }
}
