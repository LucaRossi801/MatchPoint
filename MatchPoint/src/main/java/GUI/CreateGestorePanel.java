package GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CreateGestorePanel extends JPanel {
    private Image clearImage;

    public CreateGestorePanel(CardLayout cardLayout, JPanel cardPanel) {
        // Carica l'immagine di sfondo
        URL clearImageUrl = getClass().getClassLoader().getResource("GUI/immagini/sfondohome.png");
        if (clearImageUrl != null) {
            clearImage = new ImageIcon(clearImageUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine: GUI/immagini/sfondohome.png");
        }

        // Crea pulsanti
     // Imposta il layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0); // Maggiore spaziatura tra i pulsanti
        gbc.gridx = 0; // Centra i pulsanti orizzontalmente

        Dimension buttonSize = new Dimension(400, 120); // Dimensioni personalizzate aumentate

     // Crea il primo pulsante "Inserisci Centro"
        JButton InserisciCentroButton = BackgroundPanel.createFlatButton(
            "Inserisci Centro",
            e -> cardLayout.show(cardPanel, "inserisciCentro"),
            buttonSize
        );
        InserisciCentroButton.setForeground(new Color(220, 250, 245));
        
        // Aggiungi l'icona al pulsante
        ImageIcon addIcon = caricaIcona("/GUI/immagini/add_icon.png");
        if (addIcon != null) {
        	InserisciCentroButton.setIcon(addIcon);
        	InserisciCentroButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona
        	InserisciCentroButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo
        }
        gbc.gridy = 0; // Prima riga
        add(InserisciCentroButton, gbc);

        // Crea il secondo pulsante "Modifica Centro"
        JButton ModificaCentroButton = BackgroundPanel.createFlatButton(
            "Modifica Centro",
            e -> cardLayout.show(cardPanel, "modificaCentro"),
            buttonSize
        );
        ModificaCentroButton.setForeground(new Color(220, 250, 245));
        
        // Aggiungi l'icona al pulsante
        ImageIcon editIcon = caricaIcona("/GUI/immagini/edit_icon.png");
        if (editIcon != null) {
        	ModificaCentroButton.setIcon(editIcon);
        	ModificaCentroButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona
        	ModificaCentroButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo
        }
        gbc.gridy = 1; // Seconda riga
        add(ModificaCentroButton, gbc);
        
        // Crea il terzo pulsante "VediPrenotazioni"
        JButton vediPrenotazioniButton = BackgroundPanel.createFlatButton(
            "Vedi Prenotazioni",
            e -> cardLayout.show(cardPanel, "vediPrenotazioniRicevute"),
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
        gbc.gridy = 2; // Terza riga
        add(vediPrenotazioniButton, gbc);

        // Crea il pulsante "Back"
        JButton backButton = BackgroundPanel.createFlatButton(
        	    "Back", 
        	    e -> {
        	        Login.resetFields(); // Svuota i campi di testo
        	        BackgroundPanel.showPanel("login"); // Torna al pannello di login
        	    }, 
        	    new Dimension(150, 50) // Dimensione personalizzata del bottone
        	);
        // Personalizza colore per il pulsante "Back"
        backButton.setForeground(Color.GRAY); // Sfondo grigio
        backButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
        backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo per il pulsante "Back"
        gbc.gridy = 3; // Quarta riga
        add(backButton, gbc);
        
        
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
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
        }
}
