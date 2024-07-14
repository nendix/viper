package it.unimol;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

    public static Font loadFont(String fontFileName, int style, float fontSize) {
        try {
            // Carica il font utilizzando il ClassLoader per ottenere il percorso assoluto
            InputStream is = FontLoader.class.getResourceAsStream("/fonts/" + fontFileName);
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);

            // Registra il font nell'ambiente grafico locale
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            // Ritorna il font con il formato specificato
            return customFont.deriveFont(style, fontSize);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // In caso di errore, ritorna un font di default con il formato specificato
            return new Font("Helvetica", style, (int) fontSize);
        }
    }
}
