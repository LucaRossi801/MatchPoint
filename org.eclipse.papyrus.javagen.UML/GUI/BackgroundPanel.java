package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private Image clearImage;
    private boolean imageRevealed;  // Flag per determinare se l'immagine è rivelata

    public BackgroundPanel(String blurredImagePath, String clearImagePath) {
        this.imageRevealed = false;  // L'immagine è sfocata all'inizio

        // Carica l'immagine sfocata
        URL blurredImageUrl = getClass().getResource(blurredImagePath);
        if (blurredImageUrl != null) {
            this.backgroundImage = new ImageIcon(blurredImageUrl).getImage();
        }

        // Carica l'immagine nitida
        URL clearImageUrl = getClass().getResource(clearImagePath);
        if (clearImageUrl != null) {
            this.clearImage = new ImageIcon(clearImageUrl).getImage();
        }

        // Aggiungi un listener per il click del mouse per rivelare l'immagine
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                setImageRevealed(true);  // Rivelare l'immagine nitida al click
            }
        });
    }

    // Metodo per aggiornare lo stato dell'immagine (se è rivelata o meno)
    public void setImageRevealed(boolean imageRevealed) {
        this.imageRevealed = imageRevealed;
        repaint();  // Ridisegna il pannello dopo il cambiamento
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Se l'immagine è rivelata, mostra l'immagine nitida
        if (imageRevealed && clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        } else if (backgroundImage != null) {
            // Altrimenti, mostra l'immagine sfocata
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawString("Background image not found", 10, 20);
        }
    }
}
