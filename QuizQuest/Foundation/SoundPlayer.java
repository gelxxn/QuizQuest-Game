package Foundations;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    public static void playSound(String soundFile) {
        try {
            URL url = SoundPlayer.class.getResource("/Foundations/music/" + soundFile);
            if (url == null) {
                System.err.println("Sound file not found: " + soundFile);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}