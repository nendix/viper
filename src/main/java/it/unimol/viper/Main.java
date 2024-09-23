package it.unimol.viper;

import it.unimol.viper.ui.GameFrame;
import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      GameFrame gameFrame = new GameFrame();
      gameFrame.showMenuPanel(); // Mostra il MenuPanel all'avvio
    });
  }
}
