package it.unimol.viper.app;

import java.io.*;
import javax.sound.sampled.*;

public class SoundManager {
  private Clip clip;

  public SoundManager(String soundFilePath) {
    try {
      // Utilizza ClassLoader per ottenere il file audio come InputStream
      InputStream audioSrc =
          getClass().getClassLoader().getResourceAsStream(soundFilePath);
      if (audioSrc == null) {
        throw new FileNotFoundException("Audio file not found: " +
                                        soundFilePath);
      }

      // Convert the InputStream to a BufferedInputStream to handle audio
      // loading
      InputStream bufferedIn = new BufferedInputStream(audioSrc);
      AudioInputStream audioInputStream =
          AudioSystem.getAudioInputStream(bufferedIn);

      // Ottieni il Clip e carica il file audio
      clip = AudioSystem.getClip();
      clip.open(audioInputStream);
    } catch (UnsupportedAudioFileException | IOException |
             LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public void play() {
    if (clip != null) {
      new Thread(() -> {
        stop();                   // Stop the sound if it's already playing
        clip.setFramePosition(0); // Rewind to the beginning
        clip.start();             // Start playing
      }).start();                 // Run playback in a new thread
    }
  }

  public void loop() {
    if (clip != null) {
      new Thread(() -> {
        stop();                   // Stop the sound if it's already playing
        clip.setFramePosition(0); // Rewind to the beginning
        clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the sound continuously
      }).start();                          // Run looping in a new thread
    }
  }

  public void stop() {
    if (clip != null && clip.isRunning()) {
      clip.stop(); // Stop the sound if it's playing
    }
  }
}
