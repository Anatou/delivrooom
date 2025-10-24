package fr.delivrooom.application;
import fr.delivrooom.application.model.TourSolutionSerialiser;
import fr.delivrooom.application.model.TourSolutionSerialiserIO;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IOTests {

    @Test
    void loadTourSolution() {
        String filename = "C:\\Users\\aurel\\IdeaProjects\\delivrooom\\application\\src\\test\\java\\fr\\delivrooom\\application\\tour.bin";
        TourSolutionSerialiserIO tourSolutionSerialiserIO = new TourSolutionSerialiserIO();

        try {
            TourSolutionSerialiser tourSolutionSerialiser = tourSolutionSerialiserIO.loadTourSolutionSerialization(filename);
            System.out.println(tourSolutionSerialiser);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
