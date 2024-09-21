package it.unimol.viper.ui;

import it.unimol.viper.app.FontLoader;
import it.unimol.viper.app.GameLogic;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameOverPanel extends JPanel {
  private static final int SCREEN_WIDTH = GameLogic.SCREEN_WIDTH;
  private static final int SCREEN_HEIGHT = GameLogic.SCREEN_HEIGHT;

  private GameLogic gameLogic;

  public GameOverPanel(GameLogic gameLogic, GameFrame gameFrame) {
    this.gameLogic = gameLogic;
    setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    setBackground(new Color(28, 28, 28));
    // Aggiungi il KeyListener per gestire l'evento del tasto spazio
    setFocusable(true); // Assicura che il pannello abbia il focus per catturare
                        // gli eventi da tastiera
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          gameFrame.showMenuPanel(); // Avvia il gioco quando viene premuto il
                                     // tasto spazio
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawGameOver(g);
  }

  private void drawGameOver(Graphics g) {
    // Mostra il testo "Game Over"
    g.setColor(Color.red);
    g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.BOLD, 142));
    FontMetrics metrics0 = getFontMetrics(g.getFont());
    g.drawString("GAME OVER",
                 (SCREEN_WIDTH - metrics0.stringWidth("GAME OVER")) / 2,
                 g.getFont().getSize());

    // Mostra il punteggio
    g.setColor(new Color(180, 180, 180));
    g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 76));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Score: " + gameLogic.getScore(),
                 (SCREEN_WIDTH -
                  metrics1.stringWidth("Score: " + gameLogic.getScore())) /
                     2,
                 SCREEN_HEIGHT - 650 + g.getFont().getSize());
    g.drawString(
        "High Score: " + gameLogic.getHighScore(),
        (SCREEN_WIDTH -
         metrics1.stringWidth("High Score: " + gameLogic.getHighScore())) /
            2,
        SCREEN_HEIGHT - 600 + g.getFont().getSize());

    // Mostra le statistiche delle mele mangiate
    g.setColor(new Color(180, 180, 180));
    g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 38));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString(
        "Normal Apples Eaten: " + gameLogic.getApplesEaten(),
        (SCREEN_WIDTH - metrics2.stringWidth("Normal Apples Eaten: " +
                                             gameLogic.getApplesEaten())) /
            2,
        SCREEN_HEIGHT - 500 + g.getFont().getSize());
    g.drawString(
        "Bad Apples Eaten: " + gameLogic.getBadApplesEaten(),
        (SCREEN_WIDTH - metrics2.stringWidth("Bad Apples Eaten: " +
                                             gameLogic.getBadApplesEaten())) /
            2,
        SCREEN_HEIGHT - 470 + g.getFont().getSize());
    g.drawString("Golden Apples Eaten: " + gameLogic.getGoldenApplesEaten(),
                 (SCREEN_WIDTH -
                  metrics2.stringWidth("Golden Apples Eaten: " +
                                       gameLogic.getGoldenApplesEaten())) /
                     2,
                 SCREEN_HEIGHT - 440 + g.getFont().getSize());

    // Istruzioni per tornare al menu
    g.setColor(new Color(252, 252, 252));
    g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.BOLD, 56));
    FontMetrics metrics3 = getFontMetrics(g.getFont());
    g.drawString(
        "PRESS SPACE TO RETURN TO MENU",
        (SCREEN_WIDTH - metrics3.stringWidth("PRESS SPACE TO RETURN TO MENU")) /
            2,
        SCREEN_HEIGHT - 40);
  }
}
