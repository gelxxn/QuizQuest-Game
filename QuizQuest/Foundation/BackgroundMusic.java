package Foundations;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class BackgroundMusic {
    private Clip clip;

    public void startMusic() {
        
        try {
            InputStream audioSrc = getClass().getResourceAsStream("music/music1.wav");

            if (audioSrc == null) {
                System.out.println("not found music");
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}