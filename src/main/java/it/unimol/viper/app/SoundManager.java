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
        stop();
        clip.setFramePosition(0);
        clip.start();
      }).start();
    }
  }

  public void loop() {
    if (clip != null) {
      new Thread(() -> {
        stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
      }).start();
    }
  }

  public void stop() {
    if (clip != null && clip.isRunning()) {
      clip.stop();
    }
  }
}
