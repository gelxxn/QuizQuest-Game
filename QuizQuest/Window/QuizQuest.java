package Windows;

import Foundations.BackgroundMusic;

public class QuizQuest {
    private static BackgroundMusic ms = new BackgroundMusic();
    public static void main(String[] args) {
        new HomePage();
        ms.startMusic();
    }

    public static BackgroundMusic getBackgroundMusic() {
        return ms;
    }
}