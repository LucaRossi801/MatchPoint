package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private Image clearImage;
    private boolean imageRevealed;  // Flag per determinare se l'immagine è rivelata
    private float fontSize = 24f;   // Dimensione iniziale del font
    private boolean increasing = true; // Direzione di incremento/decremento della dimensione del font
    private Timer pulseTimer;       // Timer per far pulsare la scritta

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

        // Timer per far pulsare la scritta (aumenta o diminuisce la dimensione del font)
        pulseTimer = new Timer(120, e -> {  // Timer più lento per transizioni più fluide
            // Cambia la dimensione del font con un incremento/decremento più piccolo
            if (increasing) {
                fontSize += 0.5f;  // Aumento molto lento della dimensione
                if (fontSize >= 26f) {  // Limita la dimensione massima a 26
                    increasing = false;
                }
            } else {
                fontSize -= 0.5f;  // Diminuzione molto lenta
                if (fontSize <= 24f) {  // Limita la dimensione minima a 24
                    increasing = true;
                }
            }
            repaint();  // Ridisegna il pannello per aggiornare la dimensione del testo
        });
        pulseTimer.start();  // Avvia il timer per il pulsaggio
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

            // Disegna la scritta "Clicca ovunque per continuare" sopra l'immagine sfocata
            g.setColor(Color.BLACK);
            g.setFont(new Font("Montserrat", Font.BOLD, (int) fontSize));  // Usa la dimensione pulsante del font
            String text = "Clicca ovunque per continuare".toUpperCase();  // Trasforma il testo in maiuscolo
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(text)) / 2; // Centra il testo
            int y = (int) (getHeight() / 1.5f);  // Posiziona il testo più in basso
            g.drawString(text, x, y);
        } else {
            g.drawString("Background image not found", 10, 20);
        }
    }
}
