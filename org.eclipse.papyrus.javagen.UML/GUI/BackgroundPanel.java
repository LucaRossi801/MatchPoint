package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private Image clearImage;
    private boolean imageRevealed;  // Flag per determinare se l'immagine è rivelata
    private boolean isVisible;  // Booleano per gestire la visibilità della scritta lampeggiante
    private Timer blinkTimer;   // Timer per gestire il lampeggiamento
    private int blinkInterval;  // Intervallo di tempo per il lampeggiamento

    private JButton registerButton;  // Bottone per il "Register"
    private JButton loginButton;     // Bottone per il "Login"

    public BackgroundPanel(String blurredImagePath, String clearImagePath) {
        this.imageRevealed = false;  // L'immagine è sfocata all'inizio
        this.isVisible = true;  // La scritta è visibile inizialmente
        this.blinkInterval = 800;  // Durata della visibilità della scritta (in ms)

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

        // Crea i bottoni "Register" e "Login"
        createButtons();

        // Timer per il lampeggiamento della scritta
        blinkTimer = new Timer(blinkInterval, e -> {  // Timer con intervallo variabile
            if (!imageRevealed) {  // Solo quando l'immagine non è rivelata, lampeggia la scritta
                isVisible = !isVisible;  // Alterna la visibilità della scritta
                blinkInterval = isVisible ? 300 : 800;  // Se visibile, aspetta di più (800ms), se invisibile aspetta meno (300ms)
                blinkTimer.setDelay(blinkInterval);  // Imposta il nuovo intervallo
                repaint();  // Ridisegna il pannello per aggiornare la visibilità della scritta
            }
        });
        blinkTimer.start();  // Avvia il timer per il lampeggiamento
    }

    // Metodo per creare i bottoni "Register" e "Login"
    private void createButtons() {
        // Crea il bottone "Register"
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));  // Imposta il font
        registerButton.setBackground(new Color(32, 178, 170)); // Colore verde acqua
        registerButton.setForeground(Color.WHITE);  // Colore del testo bianco
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(200, 50));  // Imposta la dimensione del bottone

        // Crea il bottone "Login"
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));  // Imposta il font
        loginButton.setBackground(new Color(32, 178, 170)); // Colore verde acqua
        loginButton.setForeground(Color.WHITE);  // Colore del testo bianco
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 50));  // Imposta la dimensione del bottone

        // Aggiungi un pannello per i bottoni con un layout semplice
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));  // Layout verticale
        buttonPanel.setBounds(0, getHeight() - 200, getWidth(), 150);  // Posiziona il pannello dei bottoni verso il basso
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        // Aggiungi il pannello dei bottoni al pannello principale
        this.setLayout(null);  // Mantieni il layout nullo
        this.add(buttonPanel);
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
        }

        // Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero
        g.setColor(Color.BLACK); // Colore nero per il contorno
        g.setFont(new Font("Impact", Font.BOLD, 70));  // Font grande e grassetto
        String matchPointText = "MATCHPOINT";  // La scritta "MATCHPOINT"
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2; // Centra orizzontalmente
        int y = 100;  // Posiziona la scritta più in alto (modifica a piacere)

        // Disegna il contorno nero, spostato di un pixel per ogni direzione
        g.drawString(matchPointText, x - 2, y - 2); // Contorno a sinistra e in alto
        g.drawString(matchPointText, x + 2, y - 2); // Contorno a destra e in alto
        g.drawString(matchPointText, x - 2, y + 2); // Contorno a sinistra e in basso
        g.drawString(matchPointText, x + 2, y + 2); // Contorno a destra e in basso

        // Disegna la scritta principale in verde acqua
        g.setColor(new Color(32, 178, 170)); // Verde acqua
        g.drawString(matchPointText, x, y); // Disegna la scritta sopra il contorno

        // Disegna la scritta "Clicca ovunque per continuare" sopra l'immagine sfocata
        if (!imageRevealed && isVisible) {  // Solo se l'immagine è sfocata e la scritta deve essere visibile
            g.setColor(Color.BLACK);
            g.setFont(new Font("Montserrat", Font.BOLD, 24));  // Usa la dimensione del font
            String text = "Clicca ovunque per continuare".toUpperCase();  // Trasforma il testo in maiuscolo
            FontMetrics metricsText = g.getFontMetrics();
            int xText = (getWidth() - metricsText.stringWidth(text)) / 2; // Centra il testo
            int yText = (int) (getHeight() / 1.2f);  // Posiziona la scritta più in basso
            g.drawString(text, xText, yText);
        }
    }
}
