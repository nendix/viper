package it.unimol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100;
    final int[] X = new int[GAME_UNITS];
    final int[] Y = new int[GAME_UNITS];
    int bodyParts = 1;
    int score;
    int badApplesEaten;
    int goldenApplesEaten;
    int appleX;
    int appleY;
    int badAppleX = -1; // Inizialmente non sul campo
    int badAppleY = -1; // Inizialmente non sul campo
    int goldenAppleX = -1; // Inizialmente non sul campo
    int goldenAppleY = -1; // Inizialmente non sul campo
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Timer badAppleTimer;
    Timer goldenAppleTimer;
    Random random;
    int highScore = 0;
    int totalApplesEaten = 0;
    int totalBadApplesEaten = 0;
    int totalGoldenApplesEaten = 0;
    int totalGamesPlayed = 0;
    int totalApples = 0;
    boolean inMenu = true;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(28, 28, 28));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        showMenu();
    }

    public void showMenu() {
        inMenu = true;
        running = false;
        repaint();
    }

    public void startGame() {
        newApple();
        running = true;
        inMenu = false;
        bodyParts = 1;
        score = 0;
        badApplesEaten = 0;
        goldenApplesEaten = 0;
        direction = 'R';
        X[0] = 0;
        Y[0] = 0;
        timer = new Timer(DELAY, this);
        timer.start();

        // Timer per le mele cattive
        badAppleTimer = new Timer(getRandomIntervalBad(), e -> {
            if (!isBadAppleOnField()) { // Verifica se non c'è già una mela cattiva sul campo
                newBadApple();
                badAppleTimer.setDelay(getRandomIntervalBad()); // Setta un nuovo intervallo casuale
            }
        });
        badAppleTimer.start();

        // Timer per le mele dorate
        goldenAppleTimer = new Timer(getRandomIntervalGolden(), e -> {
            if (!isGoldenAppleOnField()) { // Verifica se non c'è già una mela dorata sul campo
                newGoldenApple();
                goldenAppleTimer.setDelay(getRandomIntervalGolden()); // Setta un nuovo intervallo casuale
            }
        });
        goldenAppleTimer.start();
    }

    private int getRandomIntervalBad() {
        // Genera un intervallo casuale tra 10 e 30 secondi
        return random.nextInt(20000) + 10000;
    }

    private int getRandomIntervalGolden() {
        // Genera un intervallo casuale tra 45 e 60 secondi
        return random.nextInt(15000) + 45000;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkBadApple();
            checkGoldenApple();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inMenu) {
            drawMenu(g);
        } else {
            draw(g);
        }
    }

    public void draw(Graphics g) {
        if (running) {
            // Disegna la mela
            g.setColor(new Color(230, 41, 51));
            g.fillRoundRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE, 12, 12);

            // Disegna la mela cattiva
            if (badAppleX != -1 && badAppleY != -1) {
                g.setColor(new Color(132, 230, 41));
                g.fillRect(badAppleX, badAppleY, UNIT_SIZE, UNIT_SIZE);
            }

            // Disegna la mela dorata
            if (goldenAppleX != -1 && goldenAppleY != -1) {
                g.setColor(new Color(255, 255, 41));
                g.fillRect(goldenAppleX, goldenAppleY, UNIT_SIZE, UNIT_SIZE);
            }

            // Disegna il corpo del serpente
            for (int i = 0; i < bodyParts; i++) {
                g.setColor(new Color(252, 252, 252));
                g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Disegna il punteggio
            g.setColor(Color.lightGray);
            g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 30));
            g.drawString("Score: " + score, 2, SCREEN_HEIGHT - 4);
        } else {
            gameOver(g);
        }
    }

    public void drawMenu(Graphics g) {

        // Titolo del gioco
        g.setColor(new Color(252, 252, 252));
        g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 232));
        FontMetrics metrics0 = getFontMetrics(g.getFont());
        g.drawString("VIPER", (SCREEN_WIDTH - metrics0.stringWidth("VIPER")) / 2, SCREEN_HEIGHT / 4);

        // Record del punteggio
        g.setColor(Color.lightGray);
        g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 56));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("High Score: " + highScore, (SCREEN_WIDTH - metrics1.stringWidth("High Score: " + highScore)) / 2, SCREEN_HEIGHT / 3);

        // Legenda dei tasti
        g.setColor(Color.lightGray);
        g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 36));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("KEYS",  SCREEN_WIDTH / 6, SCREEN_HEIGHT / 3 + 50);
        g.drawString("W: Up",  SCREEN_WIDTH / 6 , SCREEN_HEIGHT / 3 + 80);
        g.drawString("A: Left",  SCREEN_WIDTH / 6 , SCREEN_HEIGHT / 3 + 100);
        g.drawString("S: Down",  SCREEN_WIDTH / 6 , SCREEN_HEIGHT / 3 + 120);
        g.drawString("D: Right",  SCREEN_WIDTH / 6 , SCREEN_HEIGHT / 3 + 140);

        // Legenda delle mele
        g.drawString("FOODS",  (metrics2.stringWidth("Golden Apple: -3 Length, +1 Point") / 2) - metrics2.stringWidth("FOOD") / 2, SCREEN_HEIGHT / 2 + 100);
        g.setColor(new Color(230, 41, 51));
        g.drawString("Apple: +1 Point", (metrics2.stringWidth("Golden Apple: -3 Length, +1 Point") / 2) - (metrics2.stringWidth("Apple: +1 Point") / 2) , SCREEN_HEIGHT / 2 + 130);

        g.setColor(new Color(132, 230, 41));
        g.drawString("Bad Apple: +2 Length, -1 Point", (metrics2.stringWidth("Golden Apple: -3 Length, +1 Point") / 2) - (metrics2.stringWidth("Bad Apple: +2 Length, -1 Point") / 2), SCREEN_HEIGHT / 2 + 150);

        g.setColor(new Color(255, 255, 41));
        g.drawString("Golden Apple: -3 Length, +1 Point", SCREEN_WIDTH - (SCREEN_WIDTH - 20), SCREEN_HEIGHT / 2 + 170);

        // Statistiche
        g.setColor(Color.lightGray);
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Total Games Played: " + totalGamesPlayed, SCREEN_WIDTH - metrics3.stringWidth("Total Games Played: "), SCREEN_HEIGHT / 2 + 50);
        g.drawString("Total Apples Eaten: " + totalApplesEaten, SCREEN_WIDTH - metrics3.stringWidth("Total Apples Eaten: "), SCREEN_HEIGHT / 2 + 80);
        g.drawString("Total Bad Apples Eaten: " + totalBadApplesEaten, SCREEN_WIDTH - metrics3.stringWidth("Total Bad Apples Eaten: "), SCREEN_HEIGHT / 2 + 110);
        g.drawString("Total Golden Apples Eaten: " + totalGoldenApplesEaten, SCREEN_WIDTH - metrics3.stringWidth("Total Bad Apples Eaten: "), SCREEN_HEIGHT / 2 + 140);

        // Istruzioni per iniziare una nuova partita
        g.setColor(new Color(252, 252, 252));
        g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.BOLD, 56));
        FontMetrics metrics4 = getFontMetrics(g.getFont());
        g.drawString("PRESS SPACE TO START", (SCREEN_WIDTH - metrics4.stringWidth("PRESS SPACE TO START")) / 2, SCREEN_HEIGHT - 100);
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

    public boolean isBadAppleOnField() {
        return (badAppleX != -1 && badAppleY != -1);
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
            for (int i = 0; i < bodyParts; i++) {
                if ((X[i] == goldenAppleX && Y[i] == goldenAppleY) || (appleX == goldenAppleX && appleY == goldenAppleY)) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
    }

    public boolean isGoldenAppleOnField() {
        return (goldenAppleX != -1 && goldenAppleY != -1);
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

    public void checkApple() {
        if ((X[0] == appleX) && (Y[0] == appleY)) {
            bodyParts++;
            score++;
            newApple();
        }
    }

    public void checkBadApple() {
        if ((X[0] == badAppleX) && (Y[0] == badAppleY)) {
            bodyParts += 2;
            score--;
            badApplesEaten++;
            badAppleX = -1; // Rimuove la mela cattiva dal campo
            badAppleY = -1;
        }
    }

    public void checkGoldenApple() {
        if ((X[0] == goldenAppleX) && (Y[0] == goldenAppleY)) {
            if (bodyParts > 4) {
                bodyParts -= 4;
            }
            score++;
            goldenApplesEaten++;
            goldenAppleX = -1;
            goldenAppleY = -1;
        }
    }

    public void checkCollision() {
        // Controlla se la testa collide con il corpo
        for (int i = bodyParts; i > 0; i--) {
            if ((X[0] == X[i]) && (Y[0] == Y[i])) {
                running = false;
            }
        }

        if (!running) {
            timer.stop();
            badAppleTimer.stop();
            goldenAppleTimer.stop();
            gameOver();
        }
    }

    public void gameOver() {
        totalGamesPlayed++;
        totalApplesEaten += score;
        totalBadApplesEaten += badApplesEaten;
        totalGoldenApplesEaten += goldenApplesEaten;
        totalApples += score - badApplesEaten + goldenApplesEaten;

        if (score > highScore) {
            highScore = score;
        }
    }

    public void gameOver(Graphics g) {
        // Mostra il testo "Game Over"
        g.setColor(Color.red);
        g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.BOLD, 142));
        FontMetrics metrics0 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics0.stringWidth("GAME OVER")) / 2, g.getFont().getSize());

        // Mostra il punteggio
        g.setColor(Color.lightGray);
        g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 76));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + score)) / 2, SCREEN_HEIGHT - 650 + g.getFont().getSize());
        // Mostra il record del punteggio
        g.drawString("High Score: " + highScore, (SCREEN_WIDTH - metrics1.stringWidth("High Score: " + highScore)) / 2, SCREEN_HEIGHT - 600 + g.getFont().getSize());

        g.setColor(Color.lightGray);
        g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.PLAIN, 38));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        // Mostra le statistiche delle mele mangiate
        g.drawString("Normal Apples Eaten: " + score, (SCREEN_WIDTH - metrics2.stringWidth("Normal Apples Eaten: " + score)) / 2, SCREEN_HEIGHT - 500 + g.getFont().getSize());
        g.drawString("Bad Apples Eaten: " + badApplesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Bad Apples Eaten: " + badApplesEaten)) / 2, SCREEN_HEIGHT - 470 + g.getFont().getSize());
        g.drawString("Golden Apples Eaten: " + goldenApplesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Golden Apples Eaten: " + goldenApplesEaten)) / 2, SCREEN_HEIGHT - 440 + g.getFont().getSize());

        // Istruzioni per tornare al menu
        g.setColor(new Color(252, 252, 252));
        g.setFont(FontLoader.loadFont("ByteBounce.ttf", Font.BOLD, 56));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("PRESS SPACE TO RETURN TO MENU", (SCREEN_WIDTH - metrics3.stringWidth("PRESS SPACE TO RETURN TO MENU")) / 2, SCREEN_HEIGHT - 40);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (running) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_D:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_W:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (inMenu) {
                    startGame();
                } else {
                    showMenu();
                }
            }
        }
    }

}
