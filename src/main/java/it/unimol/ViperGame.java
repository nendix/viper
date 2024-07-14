package it.unimol;

import javax.swing.*;

public class ViperGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame gameFrame = new GameFrame();
            gameFrame.showMenuPanel(); // Mostra il MenuPanel all'avvio
        });
    }
}
