package fr.delivrooom.application.model;

import java.io.*;
import java.util.List;

/**
 * @param deliveryOrder List of delivery IDs in the order they are served
 */
public record TourSolution(List<Path> paths, float totalLength, List<Long> deliveryOrder) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "TourSolution{" +
                //"paths=" + paths +
                ", totalLength=" + totalLength +
                '}';
    }

    public void save() {
        // Write
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TourSolution.bin"))) {
            oos.writeObject(this);
        }
        catch (IOException e) {
            throw new RuntimeException(e); //TODO: handle error
        }
        System.out.println("Successfully dumped TourSolution object");
    }

    public static TourSolution load() {
        // Read
        TourSolution loadedTourSolution;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TourSolution.bin"))) {
            loadedTourSolution = (TourSolution) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e); //TODO: handle error
        }
        System.out.println("Successfully loaded TourSolution object" + loadedTourSolution);

        return loadedTourSolution;
    }

}

