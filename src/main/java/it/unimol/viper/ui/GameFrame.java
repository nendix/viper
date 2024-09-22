package it.unimol.viper.ui;

import it.unimol.viper.app.GameLogic;
import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {
  private GamePanel gamePanel;
  private GameLogic gameLogic;
  private GameOverPanel gameOverPanel;
  private MenuPanel menuPanel;
  private CardLayout cardLayout;
  private JPanel mainPanel;

  public GameFrame() {
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    gameLogic = new GameLogic(this);
    gamePanel = new GamePanel(gameLogic, this);
    gameOverPanel = new GameOverPanel(gameLogic, this);
    menuPanel = new MenuPanel(gameLogic, this);

    mainPanel.add(menuPanel, "MenuPanel");
    mainPanel.add(gamePanel, "GamePanel");
    mainPanel.add(gameOverPanel, "GameOverPanel");

    add(mainPanel);
    setTitle("Viper");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);

    showMenuPanel();
  }

  public void showGameOverPanel() {
    cardLayout.show(mainPanel, "GameOverPanel");
    gameOverPanel.requestFocusInWindow();
  }

  public void showGamePanel() {
    cardLayout.show(mainPanel, "GamePanel");
    gameLogic.startGame();
    gamePanel.requestFocusInWindow();
  }

  public void showMenuPanel() {
    cardLayout.show(mainPanel, "MenuPanel");
    menuPanel.requestFocusInWindow();
  }
  public GamePanel getGamePanel() { return gamePanel; }
}
