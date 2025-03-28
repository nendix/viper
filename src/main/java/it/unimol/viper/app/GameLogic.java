package it.unimol.viper.app;

import it.unimol.viper.ui.GameFrame;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;

/**
 * GameLogic
 */
public class GameLogic {
  public static final int SCREEN_WIDTH = 1000;
  public static final int SCREEN_HEIGHT = 800;

  public static final int UNIT_SIZE = 35;
  private static final int GAME_UNITS =
      (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
  public static final int DELAY = 150;
  static final int DELAY_DECREMENT = 2;
  static final int MIN_DELAY = 50;
  private ConcurrentHashMap<String, SoundManager> soundMap;

  public int[] X = new int[GAME_UNITS];
  public int[] Y = new int[GAME_UNITS];
  private int length;
  private int score;
  private char direction = 'R';
  private char lastPressedDirection;
  private boolean running = false;
  private Timer timer;
  private Timer badAppleTimer;
  private Timer goldenAppleTimer;
  private Timer pinkAppleTimer;

  private final Random random;
  private final GameFrame gameFrame;

  private Apple apple;
  private Apple badApple;
  private Apple goldenApple;
  private Apple pinkApple;

  // statistics
  private int highScore = 0;
  private int totalGamesPlayed = 0;
  private int totalApplesEaten = 0;
  private int totalBadApplesEaten = 0;
  private int totalGoldenApplesEaten = 0;
  private int applesEaten = 0;
  private int badApplesEaten = 0;
  private int goldenApplesEaten = 0;

  public GameLogic(GameFrame gameFrame) {
    this.gameFrame = gameFrame;
    this.lastPressedDirection = 'R';
    random = new Random();
    soundMap = new ConcurrentHashMap<>();
    soundMap.put("gameStart", new SoundManager("sounds/game-start.wav"));
    soundMap.put("gameOver", new SoundManager("sounds/game-over.wav"));
    soundMap.put("apple", new SoundManager("sounds/apple.wav"));
    soundMap.put("badApple", new SoundManager("sounds/bad-apple.wav"));
    soundMap.put("goldenApple", new SoundManager("sounds/golden-apple.wav"));
    X[0] = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
    Y[0] = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    for (int i = 1; i < GAME_UNITS; i++) {
      X[i] = X[0];
      Y[i] = Y[0];
    }
  }

  public void startGame() {
    running = true;
    playSound("gameStart");
    length = 1;
    score = 0;
    applesEaten = 0;
    badApplesEaten = 0;
    goldenApplesEaten = 0;
    newApple();
    startTimers();
  }

  public void setLastPressedDirection(char direction) {
    this.lastPressedDirection = direction;
  }

  public void newApple() { apple = generateApple(Apple.AppleType.NORMAL); }

  public void checkApples() {
    checkApple(apple, Apple.AppleType.NORMAL);
    checkApple(badApple, Apple.AppleType.BAD);
    checkApple(goldenApple, Apple.AppleType.GOLDEN);
    checkApple(pinkApple, Apple.AppleType.PINK);
  }

  public void checkCollisions() {
    for (int i = 1; i < length; i++) {
      if (X[0] == X[i] && Y[0] == Y[i]) {
        running = false;
        break;
      }
    }
    if (!running) {
      timer.stop();
      badApple = null;
      badAppleTimer.stop();
      goldenApple = null;
      goldenAppleTimer.stop();
      pinkApple = null;
      pinkAppleTimer.stop();
      playSound("gameOver");
      updateStatistics();
      gameFrame.showGameOverPanel();
    }
  }

  public boolean isRunning() { return running; }
  public int getLength() { return length; }

  public int getScore() { return score; }

  public int getHighScore() { return highScore; }

  public int getTotalGamesPlayed() { return totalGamesPlayed; }

  public int getTotalApplesEaten() { return totalApplesEaten; }

  public int getTotalBadApplesEaten() { return totalBadApplesEaten; }

  public int getTotalGoldenApplesEaten() { return totalGoldenApplesEaten; }

  public int getApplesEaten() { return applesEaten; }

  public int getBadApplesEaten() { return badApplesEaten; }

  public int getGoldenApplesEaten() { return goldenApplesEaten; }

  public int getSnakeX() { return X[0]; }

  public int getSnakeY() { return Y[0]; }

  public void setDirection(char direction) { this.direction = direction; }
  public char getDirection() { return direction; }
  public Apple getApple() { return apple; }
  public Apple getBadApple() { return badApple; }
  public Apple getGoldenApple() { return goldenApple; }
  public Apple getPinkApple() { return pinkApple; }

  private void playSound(String sound) { soundMap.get(sound).play(); }
  private void startTimers() {
    timer = new Timer(DELAY, e -> {
      if (running) {
        move();
        checkApples();
        checkCollisions();
        int newDelay =
            Math.max(DELAY - Math.min(length, 50) * DELAY_DECREMENT, MIN_DELAY);
        timer.setDelay(newDelay);
        gameFrame.repaint();
      }
    });
    timer.start();

    badAppleTimer = new Timer(getRandomInterval(10000, 20000), e -> {
      badApple = generateApple(Apple.AppleType.BAD);
      badAppleTimer.setDelay(getRandomInterval(10000, 20000));
    });
    badAppleTimer.start();

    goldenAppleTimer = new Timer(getRandomInterval(45000, 60000), e -> {
      goldenApple = generateApple(Apple.AppleType.GOLDEN);
      goldenAppleTimer.setDelay(getRandomInterval(45000, 60000));
    });
    goldenAppleTimer.start();

    pinkAppleTimer = new Timer(getRandomInterval(30000, 60000), e -> {
      if (pinkApple == null) {
        pinkApple = generateApple(Apple.AppleType.PINK);
        pinkAppleTimer.setDelay(getRandomInterval(30000, 60000));
      }
    });
    pinkAppleTimer.start();
  }
  private void updateDirection() {
    if (isValidDirectionChange(direction, lastPressedDirection)) {
      direction = lastPressedDirection;
    }
  }

  private boolean isValidDirectionChange(char currentDirection,
                                         char newDirection) {
    return (currentDirection == 'L' || currentDirection == 'R') &&
        (newDirection == 'U' || newDirection == 'D') ||
        (currentDirection == 'U' || currentDirection == 'D') &&
            (newDirection == 'L' || newDirection == 'R');
  }
  private void move() {
    updateDirection();

    for (int i = length; i > 0; i--) {
      X[i] = X[i - 1];
      Y[i] = Y[i - 1];
    }

    switch (direction) {
    case 'U':
      Y[0] = Y[0] - UNIT_SIZE;
      if (Y[0] < 0) {
        Y[0] = SCREEN_HEIGHT - UNIT_SIZE;
      }
      break;
    case 'D':
      Y[0] = Y[0] + UNIT_SIZE;
      if (Y[0] >= SCREEN_HEIGHT) {
        Y[0] = 0;
      }
      break;
    case 'L':
      X[0] = X[0] - UNIT_SIZE;
      if (X[0] < 0) {
        X[0] = SCREEN_WIDTH - UNIT_SIZE;
      }
      break;
    case 'R':
      X[0] = X[0] + UNIT_SIZE;
      if (X[0] >= SCREEN_WIDTH) {
        X[0] = 0;
      }
      break;
    }
  }

  private Apple generateApple(Apple.AppleType type) {
    int x, y;
    do {
      x = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
      y = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    } while (!isValidPosition(x, y));
    return new Apple(x, y, type);
  }
  private boolean isValidPosition(int x, int y) {
    for (int i = 0; i < length; i++) {
      if (X[i] == x && Y[i] == y) {
        return false;
      }
    }
    return true;
  }

  private void checkApple(Apple a, Apple.AppleType type) {
    if (a != null && a.getX() == X[0] && a.getY() == Y[0]) {
      eatApple(type);
    }
  }
  private void eatApple(Apple.AppleType type) {
    switch (type) {
    case NORMAL:
      playSound("apple");
      score++;
      applesEaten++;
      length++;
      apple = null;
      newApple();
      break;
    case BAD:
      playSound("badApple");
      score -= 3;
      badApplesEaten++;
      length++;
      badApple = null;
      break;
    case GOLDEN:
      playSound("goldenApple");
      score += 3;
      goldenApplesEaten++;
      if (length > 2)
        length -= 2;
      goldenApple = null;
      break;
    case PINK:
      int effect = random.nextInt(3);
      switch (effect) {
      case 0:
        eatApple(Apple.AppleType.NORMAL);
        break;
      case 1:
        eatApple(Apple.AppleType.BAD);
        break;
      case 2:
        eatApple(Apple.AppleType.GOLDEN);
        break;
      }
      pinkApple = null;
      break;
    }
  }
  private void updateStatistics() {
    totalGamesPlayed++;
    totalApplesEaten += applesEaten;
    totalBadApplesEaten += badApplesEaten;
    totalGoldenApplesEaten += goldenApplesEaten;
    if (score > highScore) {
      highScore = score;
    }
  }
  private int getRandomInterval(int min, int max) {
    return random.nextInt(max - min) + min;
  }
}
