package Foundations.HANOI;

import java.io.*;

public class HanoiSave {
    private static final String FILE_NAME = "hanoi_progres8s.dat";

    public static void saveLevel(int level) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(String.valueOf(level));
        } catch (IOException e) {
            System.err.println("Error saving level: " + e.getMessage());
        }
    }
    public static int loadLevel() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return 1;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = reader.readLine();
            return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading level, resetting to Level 1.");
            return 1;
        }
    }
}