package it.unimol;

import static it.unimol.GameLogic.SCREEN_HEIGHT;
import static it.unimol.GameLogic.UNIT_SIZE;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel {
  private GameLogic gameLogic;
  private GameFrame gameFrame;

  public GamePanel(GameLogic gameLogic, GameFrame gameFrame) {
    this.gameLogic = gameLogic;
    this.gameFrame = gameFrame;
    setPreferredSize(new Dimension(GameLogic.SCREEN_WIDTH, SCREEN_HEIGHT));
    setBackground(new Color(28, 28, 28));
    setFocusable(true); // Assicura che il pannello abbia il focus per catturare
                        // gli eventi da tastiera

    // Aggiungi il KeyListener per gestire gli eventi da tastiera
    addKeyListener(new MyKeyAdapter());
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  private void draw(Graphics g) {
    if (gameLogic.isRunning()) {

      g.setColor(new Color(38, 39, 42));
      for (int x = 0; x < GameLogic.SCREEN_WIDTH; x += GameLogic.UNIT_SIZE) {
        g.drawLine(x, 0, x, GameLogic.SCREEN_HEIGHT);
      }
      // Disegna le linee orizzontali della griglia
      for (int y = 0; y < GameLogic.SCREEN_HEIGHT; y += GameLogic.UNIT_SIZE) {
        g.drawLine(0, y, GameLogic.SCREEN_WIDTH, y);
      }
      for (int i = 0; i < gameLogic.getBodyParts(); i++) {
        g.setColor(new Color(252, 252, 252));
        g.fillRect(gameLogic.X[i], gameLogic.Y[i], UNIT_SIZE, UNIT_SIZE);
      }
      // disegna la mela
      g.setColor(new Color(247, 84, 100));
      g.fillRoundRect(gameLogic.getAppleX(), gameLogic.getAppleY(), UNIT_SIZE,
                      UNIT_SIZE, 12, 12);

      // Disegna la mela cattiva
      if (gameLogic.getBadAppleX() >= 0 && gameLogic.getBadAppleY() >= 0) {
        g.setColor(new Color(97, 89, 6));
        g.fillRoundRect(gameLogic.getBadAppleX(), gameLogic.getBadAppleY(),
                        UNIT_SIZE, UNIT_SIZE, 12, 12);
      }

      // Disegna la mela dorata
      if (gameLogic.getGoldenAppleX() >= 0 &&
          gameLogic.getGoldenAppleY() >= 0) {
        g.setColor(new Color(220, 197, 17));
        g.fillRoundRect(gameLogic.getGoldenAppleX(),
                        gameLogic.getGoldenAppleY(), UNIT_SIZE, UNIT_SIZE, 12,
                        12);
      }

      // Disegna la mela casuale
      if (gameLogic.getPinkAppleX() >= 0 && gameLogic.getPinkAppleY() >= 0) {
        g.setColor(new Color(200, 125, 187));
        g.fillRoundRect(gameLogic.getPinkAppleX(), gameLogic.getPinkAppleY(),
                        UNIT_SIZE, UNIT_SIZE, 12, 12);
      }

      // Disegna il punteggio
      g.setColor(new Color(180, 180, 180));
      g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 30));
      g.drawString("Score: " + gameLogic.getScore(), 4, SCREEN_HEIGHT - 4);
      g.drawString("Length: " + gameLogic.getLength(), 120, SCREEN_HEIGHT - 4);
    }
  }

  // Classe interna per gestire gli eventi da tastiera
  private class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
      case KeyEvent.VK_A:
        if (gameLogic.getDirection() != 'R') {
          gameLogic.setDirection('L');
        }
        break;
      case KeyEvent.VK_D:
        if (gameLogic.getDirection() != 'L') {
          gameLogic.setDirection('R');
        }
        break;
      case KeyEvent.VK_W:
        if (gameLogic.getDirection() != 'D') {
          gameLogic.setDirection('U');
        }
        break;
      case KeyEvent.VK_S:
        if (gameLogic.getDirection() != 'U') {
          gameLogic.setDirection('D');
        }
        break;
      }
    }
  }
}
