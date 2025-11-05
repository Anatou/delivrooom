package fr.delivrooom.application.model;

import java.io.*;

/**
 * A utility class for saving and loading {@link TourSolutionSerialiser} objects to and from a file.
 */
public class TourSolutionSerialiserIO {
    /**
     * Default constructor for the utility class.
     */
    public TourSolutionSerialiserIO() { /* utilitaire */ }

    /**
     * Saves a {@link TourSolutionSerialiser} object to a file.
     *
     * @param serial   The object to serialize.
     * @param filename The name of the file to save to.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void saveTourSolutionSerialization(TourSolutionSerialiser serial, String filename) throws IOException {
        // Write
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(serial);
        }
        System.out.println("Successfully dumped Serialisation object");
    }

    /**
     * Loads a {@link TourSolutionSerialiser} object from a file.
     *
     * @param filename The name of the file to load from.
     * @return The deserialized {@link TourSolutionSerialiser} object.
     * @throws IOException            If an I/O error occurs while reading from the file.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public TourSolutionSerialiser loadTourSolutionSerialization(String filename) throws IOException, ClassNotFoundException {
        TourSolutionSerialiser obj;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            obj = (TourSolutionSerialiser) ois.readObject();
        }

        return obj;
    }
}
