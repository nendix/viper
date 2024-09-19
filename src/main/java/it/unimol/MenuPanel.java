package it.unimol;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuPanel extends JPanel {
  private final GameLogic gameLogic;
  private static final int SCREEN_WIDTH = GameLogic.SCREEN_WIDTH;
  private static final int SCREEN_HEIGHT = GameLogic.SCREEN_HEIGHT;

  public MenuPanel(GameLogic gameLogic, GameFrame gameFrame) {
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
          gameFrame.showGamePanel(); // Avvia il gioco quando viene premuto il
                                     // tasto spazio
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Titolo del gioco
    g.setColor(new Color(252, 252, 252));
    g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 232));
    FontMetrics metrics0 = g.getFontMetrics();
    g.drawString("VIPER", (SCREEN_WIDTH - metrics0.stringWidth("VIPER")) / 2,
                 SCREEN_HEIGHT / 4);

    // Record del punteggio
    g.setColor(new Color(180, 180, 180));
    g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 56));
    FontMetrics metrics1 = g.getFontMetrics();
    g.drawString(
        "High Score: " + gameLogic.getHighScore(),
        (SCREEN_WIDTH -
         metrics1.stringWidth("High Score: " + gameLogic.getHighScore())) /
            2,
        SCREEN_HEIGHT / 3);

    // Legenda dei tasti
    g.setColor(new Color(180, 180, 180));
    g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 36));
    g.drawString("KEYS", SCREEN_WIDTH - (SCREEN_WIDTH - 20),
                 SCREEN_HEIGHT / 3 + 50);
    g.drawString("W: Up", SCREEN_WIDTH - (SCREEN_WIDTH - 20),
                 SCREEN_HEIGHT / 3 + 80);
    g.drawString("A: Left", SCREEN_WIDTH - (SCREEN_WIDTH - 20),
                 SCREEN_HEIGHT / 3 + 100);
    g.drawString("S: Down", SCREEN_WIDTH - (SCREEN_WIDTH - 20),
                 SCREEN_HEIGHT / 3 + 120);
    g.drawString("D: Right", SCREEN_WIDTH - (SCREEN_WIDTH - 20),
                 SCREEN_HEIGHT / 3 + 140);

    // Legenda delle mele
    g.drawString("FOODS", SCREEN_WIDTH - (SCREEN_WIDTH - 20),
                 SCREEN_HEIGHT / 2 + 80);
    g.setColor(new Color(247, 84, 100));
    g.drawString("Apple: +1 Length, +1 Point",
                 SCREEN_WIDTH - (SCREEN_WIDTH - 20), SCREEN_HEIGHT / 2 + 110);
    g.setColor(new Color(133, 112, 66));
    g.drawString("Bad Apple: +1 Length, -3 Point",
                 SCREEN_WIDTH - (SCREEN_WIDTH - 20), SCREEN_HEIGHT / 2 + 130);
    g.setColor(new Color(198, 183, 0));
    g.drawString("Golden Apple: -1 Length, +3 Point",
                 SCREEN_WIDTH - (SCREEN_WIDTH - 20), SCREEN_HEIGHT / 2 + 150);
    g.setColor(new Color(200, 125, 187));
    g.drawString("Pink Apple: Random effect",
                 SCREEN_WIDTH - (SCREEN_WIDTH - 20), SCREEN_HEIGHT / 2 + 170);

    // Statistiche
    g.setColor(new Color(180, 180, 180));
    g.drawString("STATS", SCREEN_WIDTH / 2 + 100, SCREEN_HEIGHT / 2 + 80);
    g.drawString("Total Games Played: " + gameLogic.getTotalGamesPlayed(),
                 SCREEN_WIDTH / 2 + 100, SCREEN_HEIGHT / 2 + 110);
    g.drawString("Total Apples Eaten: " + gameLogic.getTotalApplesEaten(),
                 SCREEN_WIDTH / 2 + 100, SCREEN_HEIGHT / 2 + 130);
    g.drawString("Total Bad Apples Eaten: " +
                     gameLogic.getTotalBadApplesEaten(),
                 SCREEN_WIDTH / 2 + 100, SCREEN_HEIGHT / 2 + 150);
    g.drawString("Total Golden Apples Eaten: " +
                     gameLogic.getTotalGoldenApplesEaten(),
                 SCREEN_WIDTH / 2 + 100, SCREEN_HEIGHT / 2 + 170);

    // Istruzioni per iniziare una nuova partita
    g.setColor(new Color(252, 252, 252));
    g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.BOLD, 56));
    FontMetrics metrics4 = g.getFontMetrics();
    g.drawString("PRESS SPACE TO START",
                 (SCREEN_WIDTH - metrics4.stringWidth("PRESS SPACE TO START")) /
                     2,
                 SCREEN_HEIGHT - 100);
  }
}
