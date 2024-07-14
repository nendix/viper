package it.unimol;

import javax.sound.sampled.*;
import java.io.*;

public class SoundManager {
    private Clip clip;

    public SoundManager(String soundFilePath) {
        try {
            // Utilizza ClassLoader per ottenere il percorso assoluto del file audio
            ClassLoader classLoader = getClass().getClassLoader();
            File soundFile = new File(classLoader.getResource(soundFilePath).getFile());

            // Carica il file audio
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public void play() {
        if (clip != null) {
            stop(); // Stoppa il suono corrente se sta già suonando
            clip.setFramePosition(0); // Riavvolge il suono al principio
            clip.start(); // Avvia il suono
        }
    }

    public void loop() {
        if (clip != null) {
            stop(); // Stoppa il suono corrente se sta già suonando
            clip.setFramePosition(0); // Riavvolge il suono al principio
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Avvia il suono in loop continuo
        }
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop(); // Ferma il suono se sta suonando
        }
    }
}
