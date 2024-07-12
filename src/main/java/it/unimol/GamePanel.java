package it.unimol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100;
    final int[] X = new int[GAME_UNITS];
    final int[] Y = new int[GAME_UNITS];
    int bodyParts = 1;
    int applesEaten;
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

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(28, 28, 28));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
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
        draw(g);
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
                g.setColor(Color.white);
                g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
            }
            // Disegna il punteggio
            g.setColor(Color.lightGray);
            g.setFont(new Font("Helvetica", Font.PLAIN, 20));
            g.drawString("Score: " + applesEaten, 2, SCREEN_HEIGHT - 4);
        } else {
            gameOver(g);
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
            applesEaten++;
            newApple();
        }
    }

    public void checkBadApple() {
        if ((X[0] == badAppleX) && (Y[0] == badAppleY)) {
            bodyParts += 2;
            applesEaten--;
            badAppleX = -1; // Rimuove la mela cattiva dal campo
            badAppleY = -1;
        }
    }

    public void checkGoldenApple() {
        if ((X[0] == goldenAppleX) && (Y[0] == goldenAppleY)) {
            if(bodyParts >= 4) {
                bodyParts -= 3;
            }
            applesEaten += 2;
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
        }
    }

    public void gameOver(Graphics g) {
        // Mostra il punteggio
        g.setColor(Color.red);
        g.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        // Mostra il testo "Game Over"
        g.setColor(Color.red);
        g.setFont(new Font("Helvetica", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
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
        }
    }
}
