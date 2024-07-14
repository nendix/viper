package it.unimol;

import javax.swing.*;
import java.util.Random;

public class GameLogic {
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 800;
    public static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    public static final int DELAY = 200;
    static final int DELAY_DECREMENT = 5; // Decremento del DELAY per ogni punto
    static final int MIN_DELAY = 30; // Valore minimo del DELAY per limitare la velocità massima

    public int[] X = new int[GAME_UNITS];
    public int[] Y = new int[GAME_UNITS];
    private int bodyParts;
    private int appleX;
    private int appleY;
    private int badAppleX = -1;
    private int badAppleY = -1;
    private int goldenAppleX = -1;
    private int goldenAppleY = -1;
    private int pinkAppleX = -1;
    private int pinkAppleY = -1;
    private int applesEaten;
    private int badApplesEaten;
    private int goldenApplesEaten;
    private int score;
    private int highScore = 0;
    private int totalGamesPlayed = 0;
    private int totalApplesEaten = 0;
    private int totalBadApplesEaten = 0;
    private int totalGoldenApplesEaten = 0;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private final Random random;
    Timer badAppleTimer;
    Timer goldenAppleTimer;
    Timer pinkAppleTimer;
    Timer goldenAppleRemovalTimer;
    private final GameFrame gameFrame;

    public GameLogic(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        random = new Random();
    }

    private int getRandomIntervalBad() { // Genera un intervallo casuale tra 10 e 30 secondi
        return random.nextInt(20000) + 10000;
    }
    private int getRandomIntervalPink() { // Genera un intervallo casuale tra 30 e 60 secondi
        return random.nextInt(30000) + 30000;
    }
    private int getRandomIntervalGolden() { // Genera un intervallo casuale tra 45 e 60 secondi
        return random.nextInt(15000) + 45000;
    }
    public void startGame() {
        running = true;
        bodyParts = 6;
        score = 0;
        applesEaten = 0;
        badApplesEaten = 0;
        goldenApplesEaten = 0;
        direction = 'R';
        newApple();
        X[0] = 0;
        Y[0] = 0;
        timer = new Timer(DELAY, e -> {
            if (running) {
                move();
                checkApple();
                checkBadApple();
                checkGoldenApple();
                checkPinkApple();
                checkCollisions();
                // Aumenta la velocità del serpente con l'aumentare del punteggio
                int newDelay = Math.max(DELAY - score * DELAY_DECREMENT, MIN_DELAY);
                timer.setDelay(newDelay);
                gameFrame.repaint(); // Aggiorna il frame dopo ogni movimento
            }
        });
        timer.start();
        // Timer per le mele cattive
        badAppleTimer = new Timer(getRandomIntervalBad(), e -> {
            newBadApple();
            badAppleTimer.setDelay(getRandomIntervalBad()); // Setta un nuovo intervallo casuale
        });
        badAppleTimer.start();
        // Timer per le mele dorate
        goldenAppleTimer = new Timer(getRandomIntervalGolden(), e -> {
            newGoldenApple();
            goldenAppleTimer.setDelay(getRandomIntervalGolden()); // Setta un nuovo intervallo casuale
        });
        goldenAppleTimer.start();
        // Timer per le mele casuali
        pinkAppleTimer = new Timer(getRandomIntervalPink(), e -> {
            if (!isPinkAppleOnField()) { // Verifica se non c'è già una mela dorata sul campo
                newPinkApple();
                pinkAppleTimer.setDelay(getRandomIntervalPink()); // Setta un nuovo intervallo casuale
            }
        });
        pinkAppleTimer.start();
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
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

    public void newApple() {
        boolean validPosition;
        do {
            validPosition = true;
            appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            for (int i = 0; i < bodyParts; i++) {
                if (X[i] == appleX && Y[i] == appleY) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
    }
    public void newBadApple() {
        boolean validPosition;
        do {
            validPosition = true;
            badAppleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            badAppleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            for (int i = 0; i < bodyParts; i++) {
                if ((X[i] == badAppleX && Y[i] == badAppleY) || (appleX == badAppleX && appleY == badAppleY)) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
    }
    public void newGoldenApple() {
        boolean validPosition;
        do {
            validPosition = true;
            goldenAppleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            goldenAppleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            if (goldenAppleRemovalTimer != null) {
                goldenAppleRemovalTimer.stop();
            }
            goldenAppleRemovalTimer = new Timer(7000, e -> {
                goldenAppleX = -1;
                goldenAppleY = -1;
                gameFrame.repaint();
            });
            goldenAppleRemovalTimer.setRepeats(false);
            goldenAppleRemovalTimer.start();
            for (int i = 0; i < bodyParts; i++) {
                if ((X[i] == goldenAppleX && Y[i] == goldenAppleY) || (appleX == goldenAppleX && appleY == goldenAppleY)) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
    }
    public void newPinkApple() {
        boolean validPosition;
        do {
            validPosition = true;
            pinkAppleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            pinkAppleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
            for (int i = 0; i < bodyParts; i++) {
                if ((X[i] == pinkAppleX && Y[i] == pinkAppleY) || (appleX == pinkAppleX && appleY == pinkAppleY)) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
    }
    public boolean isPinkAppleOnField() { return (pinkAppleX != -1 && pinkAppleY != -1); }

    public void checkApple() {
        if (X[0] == appleX && Y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            score++;
            newApple();
        }
    }
    public void checkBadApple() {
        if ((X[0] == badAppleX) && (Y[0] == badAppleY)) {
            bodyParts += 2;
            score -= 2;
            badApplesEaten++;
            badAppleX = -1; // Rimuove la mela cattiva dal campo
            badAppleY = -1;
            System.out.println("Bad Apple Eaten!");
        }
    }
    public void checkGoldenApple() {
        if ((X[0] == goldenAppleX) && (Y[0] == goldenAppleY)) {
            if (bodyParts >= 3) {
                bodyParts -= 2;
            }
            score += 2;
            goldenApplesEaten++;
            goldenAppleX = -1;
            goldenAppleY = -1;
            System.out.println("Golden Apple Eaten!");
            if (goldenAppleRemovalTimer != null) {
                goldenAppleRemovalTimer.stop();
            }
        }
    }
    public void checkPinkApple() {
        if ((X[0] == pinkAppleX) && (Y[0] == pinkAppleY)) {
            int randomEffect = random.nextInt(3);
            switch (randomEffect) {
                case 0:
                    score++;
                    applesEaten++;
                    bodyParts++;
                    break;
                case 1:
                    bodyParts += 2;
                    score -= 2;
                    badApplesEaten++;
                    break;
                case 2:
                    if (bodyParts >= 3) {
                        bodyParts -= 2;
                    }
                    score += 2;
                    goldenApplesEaten++;
                    break;
            }
            pinkAppleX = -1; // Rimuovi la mela casuale dal campo
            pinkAppleY = -1; // Rimuovi la mela casuale dal campo
            System.out.println("Pink Apple eaten!");
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if (X[0] == X[i] && Y[0] == Y[i]) {
                running = false;
                break;
            }
        }
        if (!running) {
            timer.stop();
            badAppleTimer.stop();
            goldenAppleTimer.stop();
            pinkAppleTimer.stop();
            updateStatistics();
            gameFrame.showGameOverPanel();
        }
    }
    // Metodo per aggiornare le statistiche quando necessario
    public void updateStatistics() {
        totalGamesPlayed++;
        totalApplesEaten += applesEaten;
        totalBadApplesEaten += badApplesEaten;
        totalGoldenApplesEaten += goldenApplesEaten;

        // Aggiorna il record del punteggio
        if (score > highScore) {
            highScore = score;
        }
    }

    public int getBodyParts() { return bodyParts; }

    public int getAppleX() { return appleX; }
    public int getAppleY() { return appleY; }

    public int getBadAppleX() { return badAppleX; }
    public int getBadAppleY() { return badAppleY; }

    public int getGoldenAppleX() { return goldenAppleX; }
    public int getGoldenAppleY() { return goldenAppleY; }

    public int getPinkAppleX() { return pinkAppleX; }
    public int getPinkAppleY() { return pinkAppleY; }

    public boolean isRunning() { return running; }

    public void setDirection(char direction) { this.direction = direction; }
    public char getDirection() { return direction; }

    // Metodi per ottenere le statistiche totali
    public int getScore() { return score; }
    public int getLenght() { return bodyParts; }
    public int getHighScore() { return highScore; }
    public int getTotalGamesPlayed() { return totalGamesPlayed; }
    public int getTotalApplesEaten() { return totalApplesEaten; }
    public int getTotalBadApplesEaten() { return totalBadApplesEaten; }
    public int getTotalGoldenApplesEaten() { return totalGoldenApplesEaten; }

    public int getApplesEaten() { return applesEaten; }
    public int getBadApplesEaten() { return badApplesEaten; }
    public int getGoldenApplesEaten() { return goldenApplesEaten; }
}
