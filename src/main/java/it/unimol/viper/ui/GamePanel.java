package it.unimol.viper.ui;

import static it.unimol.viper.app.GameLogic.SCREEN_HEIGHT;
import static it.unimol.viper.app.GameLogic.UNIT_SIZE;

import it.unimol.viper.app.Apple; // Import the Apple class
import it.unimol.viper.app.FontLoader;
import it.unimol.viper.app.GameLogic;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel {
  private GameLogic gameLogic;

  public GamePanel(GameLogic gameLogic, GameFrame gameFrame) {
    this.gameLogic = gameLogic;
    setPreferredSize(new Dimension(GameLogic.SCREEN_WIDTH, SCREEN_HEIGHT));
    setBackground(new Color(28, 28, 28));
    setDoubleBuffered(true);
    setFocusable(true);

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
      for (int y = 0; y < GameLogic.SCREEN_HEIGHT; y += GameLogic.UNIT_SIZE) {
        g.drawLine(0, y, GameLogic.SCREEN_WIDTH, y);
      }
      for (int i = 0; i < gameLogic.getLength(); i++) {
        g.setColor(new Color(252, 252, 252));
        g.fillRect(gameLogic.X[i], gameLogic.Y[i], UNIT_SIZE, UNIT_SIZE);
      }

      // Draw the normal apple
      Apple normalApple = gameLogic.getApple();
      if (normalApple != null) {
        g.setColor(new Color(247, 84, 100));
        g.fillRoundRect(normalApple.getX(), normalApple.getY(), UNIT_SIZE,
                        UNIT_SIZE, 12, 12);
      }

      // Draw the bad apple
      Apple badApple = gameLogic.getBadApple();
      if (badApple != null) {
        g.setColor(new Color(97, 89, 6));
        g.fillRoundRect(badApple.getX(), badApple.getY(), UNIT_SIZE, UNIT_SIZE,
                        12, 12);
      }

      // Draw the golden apple
      Apple goldenApple = gameLogic.getGoldenApple();
      if (goldenApple != null) {
        g.setColor(new Color(220, 197, 17));
        g.fillRoundRect(goldenApple.getX(), goldenApple.getY(), UNIT_SIZE,
                        UNIT_SIZE, 12, 12);
      }

      // Draw the pink apple
      Apple pinkApple = gameLogic.getPinkApple();
      if (pinkApple != null) {
        g.setColor(new Color(200, 125, 187));
        g.fillRoundRect(pinkApple.getX(), pinkApple.getY(), UNIT_SIZE,
                        UNIT_SIZE, 12, 12);
      }

      // Draw the score
      g.setColor(new Color(180, 180, 180));
      g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 30));
      g.drawString("Score: " + gameLogic.getScore(), 4, SCREEN_HEIGHT - 4);
      g.drawString("Length: " + gameLogic.getLength(), 120, SCREEN_HEIGHT - 4);
    }
  }

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
