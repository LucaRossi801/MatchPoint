package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CreateGestorePanel extends JPanel {
    private Image clearImage;

    public CreateGestorePanel(CardLayout cardLayout, JPanel cardPanel) {
        // Carica l'immagine una sola volta
        URL clearImageUrl = getClass().getResource("/GUI/immagini/sfondohome.png");
        if (clearImageUrl != null) {
            clearImage = new ImageIcon(clearImageUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine: " + "/GUI/immagini/sfondohome.png");
        }

        //Imposta il layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0); // Maggiore spaziatura tra i pulsanti
        gbc.gridx = 0; // Centra i pulsanti orizzontalmente

        Dimension buttonSize = new Dimension(300, 90); // Dimensioni personalizzate aumentate

        //Crea il primo pulsante "Inserisci Centro"
        JButton inserisciCentroButton = new JButton("Inserisci Centro", caricaIcona("/GUI/immagini/add_icon.png"));
        personalizzaBottone(inserisciCentroButton, cardLayout, cardPanel, "inserisciCentro", buttonSize);
        gbc.gridy = 0; // Prima riga
        add(inserisciCentroButton, gbc);

        //Crea il secondo pulsante "Modifica Centro"
        JButton modificaCentroButton = new JButton("Modifica Centro", caricaIcona("/GUI/immagini/edit_icon.png"));
        personalizzaBottone(modificaCentroButton, cardLayout, cardPanel, "modificaCentro", buttonSize);
        gbc.gridy = 1; // Seconda riga
        add(modificaCentroButton, gbc);

        //Crea il terzo pulsante "Vedi Prenotazioni"
        JButton vediPrenotazioniButton = new JButton("Vedi Prenotazioni", caricaIcona("/GUI/immagini/list_icon.png"));
        personalizzaBottone(vediPrenotazioniButton, cardLayout, cardPanel, "vediPrenotazioni", buttonSize);
        gbc.gridy = 2; // Terza riga
        add(vediPrenotazioniButton, gbc);

        //Crea il pulsante "Back"
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo
        backButton.setBackground(Color.GRAY); // Colore di sfondo grigio
        backButton.setForeground(Color.WHITE); // Testo bianco
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(150, 50)); // Dimensioni più compatte (meno alto)
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "login")); // Torna al pannello "login"
        gbc.gridy = 3; // Quarta riga
        add(backButton, gbc);

    }

    private void personalizzaBottone(JButton button, CardLayout cardLayout, JPanel cardPanel, String targetCard, Dimension size) {
        button.setFont(new Font("Arial", Font.BOLD, 24)); // Font leggermente più grande
        button.setBackground(new Color(32, 178, 170));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(size); // Imposta la dimensione personalizzata
        button.setHorizontalTextPosition(SwingConstants.RIGHT); // Posiziona il testo a destra dell'icona
        button.setIconTextGap(15); // Spaziatura tra l'icona e il testo
        button.addActionListener(e -> cardLayout.show(cardPanel, targetCard)); // Azione per navigare
    }

    private ImageIcon caricaIcona(String percorso) {
        URL iconUrl = getClass().getResource(percorso);
        if (iconUrl != null) {
            ImageIcon icon = new ImageIcon(iconUrl);
            // Ridimensiona l'immagine
            Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.out.println("Errore nel caricamento dell'icona: " + percorso);
            return null; // Restituisci null se l'icona non viene trovata
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Disegna l'immagine di sfondo
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
