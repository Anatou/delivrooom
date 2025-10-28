package fr.delivrooom.application;
import fr.delivrooom.application.model.TourSolutionSerialiser;
import fr.delivrooom.application.model.TourSolutionSerialiserIO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class IOTests {

    @Test
    void loadTourSolution() {

        File file = new File("src/test/java/fr/delivrooom/application/tour.bin");

        String filename = file.getAbsolutePath();
        TourSolutionSerialiserIO tourSolutionSerialiserIO = new TourSolutionSerialiserIO();

        try {
            TourSolutionSerialiser tourSolutionSerialiser = tourSolutionSerialiserIO.loadTourSolutionSerialization(filename);
            assert (tourSolutionSerialiser.hashCode() == -304933208);


        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
