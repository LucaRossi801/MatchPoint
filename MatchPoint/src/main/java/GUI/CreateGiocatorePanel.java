package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CreateGiocatorePanel extends JPanel {
    private Image clearImage;

    public CreateGiocatorePanel(CardLayout cardLayout, JPanel cardPanel) {
        // Carica l'immagine una sola volta
        URL clearImageUrl = getClass().getResource("/GUI/immagini/sfondohome.png");
        if (clearImageUrl != null) {
            clearImage = new ImageIcon(clearImageUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine: " + "/GUI/immagini/sfondohome.png");
        }

        // Imposta il layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0); // Maggiore spaziatura tra i pulsanti
        gbc.gridx = 0; // Centra i pulsanti orizzontalmente

        Dimension buttonSize = new Dimension(400, 120); // Dimensioni personalizzate aumentate

     // Crea il primo pulsante "Prenota"
        JButton prenotaButton = BackgroundPanel.createFlatButton(
            "Inserisci Prenotazione",
            e -> cardLayout.show(cardPanel, "inserisciPrenotazione"),
            buttonSize
        );
        prenotaButton.setForeground(new Color(220, 250, 245));
        
        // Aggiungi l'icona al pulsante
        ImageIcon addIcon = caricaIcona("/GUI/immagini/add_icon.png");
        if (addIcon != null) {
            prenotaButton.setIcon(addIcon);
            prenotaButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona
            prenotaButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo
        }
        gbc.gridy = 0; // Prima riga
        add(prenotaButton, gbc);

        // Crea il secondo pulsante "Vedi Prenotazioni"
        JButton vediPrenotazioniButton = BackgroundPanel.createFlatButton(
            "Vedi Prenotazioni",
            e -> cardLayout.show(cardPanel, "vediPrenotazioniFatte"),
            buttonSize
        );
        vediPrenotazioniButton.setForeground(new Color(220, 250, 245));
        
        // Aggiungi l'icona al pulsante
        ImageIcon listIcon = caricaIcona("/GUI/immagini/list_icon.png");
        if (listIcon != null) {
            vediPrenotazioniButton.setIcon(listIcon);
            vediPrenotazioniButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona
            vediPrenotazioniButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo
        }
        gbc.gridy = 1; // Seconda riga
        add(vediPrenotazioniButton, gbc);

        // Crea il pulsante "Back"
        JButton backButton = BackgroundPanel.createFlatButton(
            "Back",
            e -> cardLayout.show(cardPanel, "login"),
            new Dimension(150, 50)
        );
        // Personalizza colore per il pulsante "Back"
        backButton.setForeground(Color.GRAY); // Sfondo grigio
        backButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
        backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo per il pulsante "Back"
        gbc.gridy = 3; // Quarta riga
        add(backButton, gbc);

    }
    /**
     * Metodo per caricare un'icona PNG e ridimensionarla.
     */
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
