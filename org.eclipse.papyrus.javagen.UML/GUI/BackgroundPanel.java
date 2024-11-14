package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;  // Pannello per la homepage (con l'immagine sfocata e la scritta lampeggiante)
    private JPanel mainPanel;  // Pannello per la vista successiva (con l'immagine nitida e i bottoni)

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

        // Usa CardLayout per gestire le diverse viste
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Crea i due pannelli per le due viste
        homePanel = createHomePanel(blurredImagePath);
        mainPanel = createMainPanel(clearImagePath);

        // Aggiungi i pannelli al CardLayout
        cardPanel.add(homePanel, "home");
        cardPanel.add(mainPanel, "main");

        // Imposta la vista iniziale come la homepage
        cardLayout.show(cardPanel, "home");

        // Imposta il layout del pannello principale
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        // Imposta la dimensione del pannello (ad esempio 800x600)
        setPreferredSize(new Dimension(800, 600));  // Imposta una dimensione esplicita per il pannello
    }

    // Crea il pannello della homepage con immagine sfocata e scritta lampeggiante
    private JPanel createHomePanel(String blurredImagePath) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Disegna l'immagine sfocata
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.out.println("Immagine sfocata non trovata!");
                }

                // Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero
                g.setColor(Color.BLACK); // Contorno nero
                g.setFont(new Font("Impact", Font.BOLD, 70));  // Font grande e grassetto
                String matchPointText = "MATCHPOINT";
                FontMetrics metrics = g.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
                int y = 100;

                // Contorno nero per la scritta
                g.drawString(matchPointText, x - 2, y - 2);
                g.drawString(matchPointText, x + 2, y - 2);
                g.drawString(matchPointText, x - 2, y + 2);
                g.drawString(matchPointText, x + 2, y + 2);

                // Scritta principale in verde acqua
                g.setColor(new Color(32, 178, 170));
                g.drawString(matchPointText, x, y);

                // Scritta "Clicca ovunque per continuare"
                if (isVisible) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Montserrat", Font.BOLD, 24));
                    String text = "Clicca ovunque per continuare".toUpperCase();
                    FontMetrics metricsText = g.getFontMetrics();
                    int xText = (getWidth() - metricsText.stringWidth(text)) / 2;
                    int yText = (int) (getHeight() * 0.75);
                    g.drawString(text, xText, yText);
                }
            }
        };

        // Carica l'immagine sfocata
        URL blurredImageUrl = getClass().getResource(blurredImagePath);
        if (blurredImageUrl != null) {
            this.backgroundImage = new ImageIcon(blurredImageUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine sfocata: " + blurredImagePath);
        }

        // Timer per il lampeggiamento della scritta
        blinkTimer = new Timer(blinkInterval, e -> {
            if (!imageRevealed) {
                isVisible = !isVisible;
                blinkInterval = isVisible ? 300 : 800;
                blinkTimer.setDelay(blinkInterval);
                repaint();
            }
        });
        blinkTimer.start();

        // Aggiungi il mouse listener
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(cardPanel, "main");  // Passa alla vista principale
            }
        });

        return panel;
    }

    // Crea il pannello della vista principale con immagine nitida e bottoni
    private JPanel createMainPanel(String clearImagePath) {
        JPanel panel = new JPanel(null) {  // Usa un layout null per posizionare i bottoni manualmente
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Disegna l'immagine nitida
                if (clearImage != null) {
                    g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
                }

                // Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero
                g.setColor(Color.BLACK); // Contorno nero
                g.setFont(new Font("Impact", Font.BOLD, 70));  // Font grande e grassetto
                String matchPointText = "MATCHPOINT";
                FontMetrics metrics = g.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
                int y = 100;

                // Contorno nero per la scritta
                g.drawString(matchPointText, x - 2, y - 2);
                g.drawString(matchPointText, x + 2, y - 2);
                g.drawString(matchPointText, x - 2, y + 2);
                g.drawString(matchPointText, x + 2, y + 2);

                // Scritta principale in verde acqua
                g.setColor(new Color(32, 178, 170));
                g.drawString(matchPointText, x, y);
            }
        };

        // Carica l'immagine nitida
        URL clearImageUrl = getClass().getResource(clearImagePath);
        if (clearImageUrl != null) {
            this.clearImage = new ImageIcon(clearImageUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine nitida: " + clearImagePath);
        }

        // Crea i bottoni "Register" e "Login"
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        registerButton.setBackground(new Color(32, 178, 170));  // Colore verde acqua
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBounds(300, 400, 200, 50); // Posizione
        panel.add(registerButton);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginButton.setBackground(new Color(32, 178, 170));  // Colore verde acqua
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBounds(300, 470, 200, 50); // Posizione
        panel.add(loginButton);

        return panel;
    }
}


