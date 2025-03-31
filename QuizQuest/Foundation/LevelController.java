package Foundations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LevelController {
    //Make Change To Another Level
    private static final String FILE_NAME = "LavelProgress.dat";

    public static void saveLevelP(int levelP) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) { 
            writer.write(String.valueOf(levelP));
        } catch (IOException e) {
            System.err.println("Error saving level: " + e.getMessage());
        }
    }

    public static int loadLevelPuzzle() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = reader.readLine();
            return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading level, resetting to Level 1.");
            return 0;
        }
    }
}